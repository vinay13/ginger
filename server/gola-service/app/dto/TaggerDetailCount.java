package dto;

import java.util.List;

/**
 * Created by Welcome on 12-Aug-2017.
 */
public class TaggerDetailCount {
    private String taggerId;
    private Integer gifCount;
    private String gifId;

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public String getTaggerId() {
        return taggerId;
    }

    public void setTaggerId(String taggerId) {
        this.taggerId = taggerId;
    }

    public Integer getGifCount() {
        return gifCount;
    }

    public void setGifCount(Integer gifCount) {
        this.gifCount = gifCount;
    }
}
