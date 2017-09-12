package dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by senthil
 */
public class TagTopItemResponse {

    private Map<String, TopItemData> results = new HashMap<>();



    public Map<String, TopItemData> getResults() {
        return results;
    }

    public void setResults(Map<String, TopItemData> results) {
        this.results = results;
    }
}
