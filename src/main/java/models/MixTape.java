package models;

import java.util.List;

/**
 * Created by hshankar on 12/12/19.
 */
public class MixTape {
    private List<Users> users;
    private List<PlayList> playlists;
    private List<Song> songs;

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public List<PlayList> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlayList> playlists) {
        this.playlists = playlists;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "mixTapeObj{" +
                "users=" + users.toString() +
                ", playlists=" + playlists.toString() +
                ", songs=" + songs.toString() +
                '}';
    }
}
