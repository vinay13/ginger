package dto;

/**
 * Created by senthil
 */
public class GifIdiomStat {

    private String idiom;
    private Long count;
    private Long enabledCount;
    private Long taggingRequiredCount;

    public GifIdiomStat(String idiom, Long count, Long enabledCount, Long taggingRequiredCount) {
        this.idiom = idiom;
        this.count = count;
        this.enabledCount = enabledCount;
        this.taggingRequiredCount = taggingRequiredCount;
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getEnabledCount() {
        return enabledCount;
    }

    public void setEnabledCount(Long enabledCount) {
        this.enabledCount = enabledCount;
    }

    public Long getTaggingRequiredCount() {
        return taggingRequiredCount;
    }

    public void setTaggingRequiredCount(Long taggingRequiredCount) {
        this.taggingRequiredCount = taggingRequiredCount;
    }
}
