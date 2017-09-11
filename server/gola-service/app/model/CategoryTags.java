package model;

import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class CategoryTags {

    @Id
    private String id;
    private String category;
    private List<String> tags = new LinkedList<String>();

    public CategoryTags(String category, List<String> tags) {
        this.category = category;
        this.tags = tags;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
