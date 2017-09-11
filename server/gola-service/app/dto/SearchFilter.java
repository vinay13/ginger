package dto;

import model.Idiom;

/**
 * Created by senthil
 */
public class SearchFilter {

    private String source;
    private Idiom idiom;
    private boolean tagsPresent;
    private String tag;
    private String publishedBy;
    private BooleanSearchCriteria tagCorrectionRequired;

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Idiom getIdiom() {
        return idiom;
    }

    public void setIdiom(Idiom idiom) {
        this.idiom = idiom;
    }

    public boolean isTagsPresent() {
        return tagsPresent;
    }

    public void setTagsPresent(boolean tagsPresent) {
        this.tagsPresent = tagsPresent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BooleanSearchCriteria getTagCorrectionRequired() {
        return tagCorrectionRequired;
    }

    public void setTagCorrectionRequired(BooleanSearchCriteria tagCorrectionRequired) {
        this.tagCorrectionRequired = tagCorrectionRequired;
    }
}
