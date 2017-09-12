package dto;

import model.Idiom;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GifMetaDataDto {

    private String id;
    private String baseUrl;
    private String originalFN;
    private String lowResFN;
    private String lowResWebpFN;
    private String thumbNailFN;

    private int width;
    private int height;
    private int size;
    private int lowResSize;
    private int lowResWidth;
    private int lowResHeight;
    private int shareCount;

 /*   private String displayName;
    private String tag;*/

    private String source;
    private List<Idiom> idioms;
    private List<String> tags;
    private String publishedBy;
    private Date publishedOn;
    private Boolean active;

    public GifMetaDataDto(String id, String baseUrl, String originalFN, String lowResFN, String lowResWebpFN, String thumbNailFN) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.originalFN = originalFN;
        this.lowResFN = lowResFN;
        this.lowResWebpFN = lowResWebpFN;
        this.thumbNailFN = thumbNailFN;
    }

    public GifMetaDataDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOriginalFN() {
        return originalFN;
    }

    public void setOriginalFN(String originalFN) {
        this.originalFN = originalFN;
    }

    public String getLowResFN() {
        return lowResFN;
    }

    public void setLowResFN(String lowResFN) {
        this.lowResFN = lowResFN;
    }

    public String getLowResWebpFN() {
        return lowResWebpFN;
    }

    public void setLowResWebpFN(String lowResWebpFN) {
        this.lowResWebpFN = lowResWebpFN;
    }

    public String getThumbNailFN() {
        return thumbNailFN;
    }

    public void setThumbNailFN(String thumbNailFN) {
        this.thumbNailFN = thumbNailFN;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLowResWidth() {
        return lowResWidth;
    }

    public void setLowResWidth(int lowResWidth) {
        this.lowResWidth = lowResWidth;
    }

    public int getLowResHeight() {
        return lowResHeight;
    }

    public void setLowResHeight(int lowResHeight) {
        this.lowResHeight = lowResHeight;
    }


    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public List<Idiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<Idiom> idioms) {
        this.idioms = idioms;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLowResSize() {
        return lowResSize;
    }

    public void setLowResSize(int lowResSize) {
        this.lowResSize = lowResSize;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
}
