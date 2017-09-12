package dto;

/**
 * Created by senthil
 */
public class GifTrendingData {

    private String gifId;
    private int shareCount;
    private int viewCount;

    private int favCount;

    public GifTrendingData(String gifId, int shareCount, int viewCount, int favCount) {
        this.gifId = gifId;
        this.shareCount = shareCount;
        this.viewCount = viewCount;
        this.favCount = favCount;
    }

    public GifTrendingData() {
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }
}
