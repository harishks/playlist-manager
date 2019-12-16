package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

/**
 * Created by hshankar on 12/12/19.
 */
public class PlayList {
    private String id;
    @JsonProperty("user_id")
    private String uid;
    @JsonProperty("song_ids")
    private HashSet<String> sids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashSet<String> getSids() {
        return sids;
    }

    public void setSids(HashSet<String> sids) {
        this.sids = sids;
    }
}
