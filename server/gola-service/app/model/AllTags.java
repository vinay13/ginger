package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by senthil
 */
public class AllTags {
    private String id;
    private Set<String> tags = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
