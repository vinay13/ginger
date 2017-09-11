package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Created by senthil
 */
@Document
public class FavouriteGif {

    @Id
    private String id;

    private Date timestamp;

    private Map<String, Date> gifs = new HashMap<>();

    @Indexed
    private String emailId;

    public FavouriteGif() {
    }

    public FavouriteGif(String emailId) {
        this.emailId = emailId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Date> getGifs() {
        return gifs;
    }

    public void setGifs(Map<String, Date> gifs) {
        this.gifs = gifs;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
