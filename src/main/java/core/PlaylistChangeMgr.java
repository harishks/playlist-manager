package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by hshankar on 12/12/19.
 */
public class PlaylistChangeMgr {
    private MixTape mt;
    private ObjectMapper objectMapper;
    private String changesFile;
    private String outFile;
    private HashMap<String, PlayList> map;
    private HashSet<String> userIds;
    private HashSet<String> songIds;

    public PlaylistChangeMgr(MixTape mt, String changesFile, String outFile) {
        this.mt = mt;
        this.objectMapper = new ObjectMapper();
        this.changesFile = changesFile;
        this.outFile = outFile;
        this.map = new HashMap<>();
        /* Map to lookup Playlist by ID */
        for (PlayList p : mt.getPlaylists()) {
            map.put(p.getId(), p);
        }
        this.userIds = new HashSet<>();
        /* Set to validate users by ID */
        for (Users u : mt.getUsers()) {
            userIds.add(u.getId());
        }
        this.songIds = new HashSet<>();
        /* Set to verify if the songs in the changelist exists in the input */
        for (Song s : mt.getSongs()) {
            songIds.add(s.getId());
        }
    }

    private boolean isUserValid(String id) {
        return userIds.contains(id);
    }

    private boolean isPlaylistValid(String pid) {
        return map.containsKey(pid);
    }

    private boolean isSongValid(String sid) {
        return songIds.contains(sid);
    }

    public void applyChanges() throws IOException {
        File file = new File(changesFile);
        ChangesList cl = objectMapper.readValue(file, ChangesList.class);
        /* Process changes related to appending entries */
        for (PlayList p : cl.getAppend().getPlaylists()) {
            if (isPlaylistValid(p.getId())) {
                for (String sid : p.getSids()) {
                    if (isSongValid(sid))
                        addSong(p.getId(), sid);
                }
            }
        }
        /* Process changes related to creating entries */
        for (PlayList p : cl.getCreate().getPlaylists()) {
            /* Create playlists only for existing users */
            if (isUserValid(p.getUid())) {
                mt.getPlaylists().add(p);
                map.put(p.getId(), p);
            }
        }
        /* Process changes related to removing entries
         * NOTE: 'remove' ops should not be applied before 'append' */
        for (String id : cl.getRemove().getPids()) {
            /* Remove any existing playlists */
            if (isPlaylistValid(id))
                removePlayList(map.get(id));
        }

        /* Write the modified context to an output file */
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                new FileOutputStream(outFile), mt);
    }

    private void addSong(String playlistId, String songId) {
        PlayList p = map.get(playlistId);
        p.getSids().add(songId);
    }

    private void removePlayList(PlayList p) {
        mt.getPlaylists().remove(p);
    }
}
