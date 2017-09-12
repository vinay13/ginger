package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class RelatedGifRequest {

    private List<String> tags = new LinkedList<>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
