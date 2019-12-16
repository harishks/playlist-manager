# Music Playlist Manager
# Introduction
This application is designed to consume a file containing updates/changes that need to be applied on the input 
mixtape file. The application loads the input information supplied by the user - containing song, users, and playlist 
associations. The application validates the incoming changes and all the valid changes are applied to the input 
playlist object in a single pass. 

# Design

The application uses the user supplied "input.json" file to build a global view of the 
 mixtape. At a high level, mixtape consists of three main components viz. list of songs, list of users,
 and a list of playlists associated to existing users. The init phase consists of building an
 in-memory context object from the supplied input file using Jackson core and annotations APIs.
 Jackson provides a lightweight, flexible, and a fast way to convert JSON string to/from a simple POJO.
 
### **Code Structure**
    
   The code is divided into two packages:
        
   1. **core** - contains two classes `PlaylistEditorClientShell` and `PlaylistChangeMgr`to process input 
     arguments from command line and apply the changes supplied in "changes.json" file in a single pass
   2. **models** - contains POJOs to convert JSON strings in "input.json" and "changes.json" to an in-memory
     representation
        
### **Data Structures** 

   `PlaylistChangeMgr` class uses the following data-structures to improve runtime efficiency 
    while applying the changes to the input:
    
   1. Hash table to facilitate O(1) lookups while removing Playlists for a given user, and adding a new song to an existing playlist
   2. Set to keep track of all the existing users by `user_id` - used to validate users while 
    adding a new playlist
   3. Set to keep track of all the existing songs in the system - used to verify the existence of a song while appending a song to the playlist

### **Structure of "changes.json" file**

   The change file is modeled to be extensible for any kind of add, append, or remove operations on the input mixtape.
     For the use-case in hand, we have an option to add new playlists with existing songs, append a new song to an existing
     playlist, and remove a playlist for an existing user. Any of these change operations can be represented as an array, (i.e.)
     multiple changes for any given operation can be bundled together to be applied in a serial fashion. Note that, the serial execution
     model is used in the current design to keep the implementation simple. For eg., a remove operation consists of an array of "pids" (playlist ids), which could span across multiple users.
     
### **Validations while applying changes**

   1. In the current implementation, we validate the incoming playlists in the changelist by `playlist_id` to ensure 
    that the playlist being modified exists in the input mixtape. 
   2. Similarly, users and songs in the changelist are compared against the users and songs in the input mixtape before creating a new playlist for the given `user_id`  
    NOTE: The current implementation does not throw an exception or log the error when an invalid input is detected. It silently ignores the invalid input and moves on with the rest of the changes.  

### **Usage** 
 
   The current implementation of the launcher script expects the user to run the following shell script from the root of the project directory.
    
    $ bin/playlistmgr.sh <input_filename> <changes_filename> <output_filename>
    
   The launcher scripts requires execute permissions to run, which could be enabled by using the following cmd.
    
    $ chmod +x bin/playlistmgr.sh
 
    SJCMACXRWWG8WN:playlistconversions hshankar$ ls
    README.md		changes.json		out			output.json		pom.xml			target
    bin			inp.json		out.json		playlistconversions.iml	src
    SJCMACXRWWG8WN:playlistconversions hshankar$ bin/playlistmgr.sh inp.json changes.json out.json
 
 ### **Dependencies** 
 
   The application uses Jackson data processing API to serialize/deserialize JSON strings.
    

# Scaling Considerations

A scaled out version of this solution would warrant a different on-disk data model. The input file can be used to populate entries in three different
tables viz. "users" table, "songs" table, and "playlists" table, wherein each table can be sharded/partitioned by their id-ranges (key-range partitioning). 
We could use the "users" table to store all the registered users, wherein each user is associated to a unique `user_id`. Similarly, we could have a table to 
store all the songs indexed by a unique `song_id`. 
This way, we would be able to independently scale the resources around storing "users" and "songs" in the system based on their respective cardinalities. A frontend query service could be designed that exposes APIs to lookup entries by `user_id` and `song_id` fields.
The query service could be a lightweight request routing layer, which queries a metadata layer based by a coordination framework like ZooKeeper, to get the data placement information viz. nodes/shards housing the `ids` requested in the query.
This metadata layer would be updated as part of datastore updates/inserts. Similarly, we could have a global `playlists` table to store all the unique playlists in the systems and reference an array of playlists in the "users" table to keep track 
 of all the playlists created by the given user. Updates to these tables could be applied by exposing REST APIs in the query service.
 The query service can be scaled horizontally based on the expected update/query rates. Updates could be channelized into the query service using a queue service like Kafka to 
 streamline the processing pipeline for the incoming updates from users. 
 
  
 
 
