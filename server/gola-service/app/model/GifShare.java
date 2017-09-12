package model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class GifShare {

    private String id;
    private String gifId;
    private List<String> tags = new LinkedList<>();
    private List<Idiom> idioms = new LinkedList<>();
    private Date timestamp;
    private boolean active = true;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Idiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<Idiom> idioms) {
        this.idioms = idioms;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
