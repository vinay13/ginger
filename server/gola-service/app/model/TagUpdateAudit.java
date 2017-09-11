package model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class TagUpdateAudit {

    private String id;
    private String gifId;
    private Date timestamp;
    private String taggerId;
    private List<String> tagsAdded = new LinkedList<>();
    private List<String> tagsRemoved = new LinkedList<>();

    public TagUpdateAudit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTaggerId() {
        return taggerId;
    }

    public void setTaggerId(String taggerId) {
        this.taggerId = taggerId;
    }

    public List<String> getTagsAdded() {
        return tagsAdded;
    }

    public void setTagsAdded(List<String> tagsAdded) {
        this.tagsAdded = tagsAdded;
    }

    public List<String> getTagsRemoved() {
        return tagsRemoved;
    }

    public void setTagsRemoved(List<String> tagsRemoved) {
        this.tagsRemoved = tagsRemoved;
    }
}
