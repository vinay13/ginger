package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class UploadFromUrlRequest {

    private List<String> idioms = new LinkedList<>();
    private List<String> tags = new LinkedList<>();
    private List<String> categories = new LinkedList<>();
    private String url;

    public List<String> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<String> idioms) {
        this.idioms = idioms;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
