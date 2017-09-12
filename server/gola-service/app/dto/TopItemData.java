package dto;

/**
 * Created by senthil
 */
public class TopItemData {

    private String id;

    private String url;

    public TopItemData() {
    }

    public TopItemData(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TrendingResult{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
