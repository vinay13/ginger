package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by senthil
 */

@Document
public class TrendingGif {

    @Id
    private String id;
    private Map<String, Integer> hits = new HashMap<String, Integer>();


    public TrendingGif() {
    }

    public TrendingGif(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Integer> getHits() {
        return hits;
    }

    public void setHits(Map<String, Integer> hits) {
        this.hits = hits;
    }
}
