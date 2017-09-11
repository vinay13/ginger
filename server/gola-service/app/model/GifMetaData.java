package model;

import dto.GifConversionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */

@Document
public class GifMetaData {

    @Id
    private String id;
    //private String url;// Watermarked Gif URL

    //private String originalUrl; // Original Gif URL as it is

    private String baseUrl;
    private String originalFN;
    private String lowResFN;
    private String lowResWebpFN;
    private String thumbNailFN;

    private String waterMarkedFN;


    @Indexed(direction = IndexDirection.DESCENDING)
    private Date publishedOn;

    @Indexed
    private List<String> tags = new LinkedList<String>();
    private List<String> searchTags = new LinkedList<String>();

    /*private String lowResUrl;
    private String lowResWebpUrl;
    private String thumbNailUrl;
*/

    private List<Idiom> idioms = new LinkedList<>();
    private int size;
    private String sha256;
    private String source;

    @Indexed
    private String publishedBy;
    private String sourceUrl;
    private boolean active = false;
    private int width;
    private int height;

    private int lowResWidth;
    private int lowResHeight;
    private int lowResSize;
    private int shareCount;
    private int favCount;
    private int viewCount;
    private boolean tagCorrected;

    private String triggerExpr;

    public GifMetaData(String uuid, String baseUrl, String originalFN, Date publishedOn, List<String> tags,
                       List<Idiom> idioms, int size, String computedSha256, String emailId, boolean active){
        this.id = uuid;

        this.publishedOn = publishedOn;
        this.tags = tags;

        this.idioms = idioms;
        this.size =  size;
        this.sha256 = computedSha256;
        this.publishedBy = emailId;
        this.active = active;
        this.baseUrl = baseUrl;
        this.originalFN = originalFN;
//        setOriginalUrl(fullUrl);
    }

    public GifMetaData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public String getUrl() {
        if(url == null) return originalUrl;
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
*/
    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    public List<Idiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<Idiom> idioms) {
        this.idioms = idioms;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

  /*  public String getLowResUrl() {
        return lowResUrl;
    }

    public void setLowResUrl(String lowResUrl) {
        this.lowResUrl = lowResUrl;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }
*/
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

    public int getLowResSize() {
        return lowResSize;
    }

    public void setLowResSize(int lowResSize) {
        this.lowResSize = lowResSize;
    }

    public String getTriggerExpr() {
        return triggerExpr;
    }

    public void setTriggerExpr(String triggerExpr) {
        this.triggerExpr = triggerExpr;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isTagCorrected() {
        return tagCorrected;
    }

    public void setTagCorrected(boolean tagCorrected) {
        this.tagCorrected = tagCorrected;
    }

    public boolean isWaterMarked(){
        return waterMarkedFN != null && !waterMarkedFN.equals(originalFN);
    }

    public boolean hasThumbNail(){
        return thumbNailFN != null;
    }

    public boolean hasLowRes(){
        return lowResFN != null;
    }


    public boolean hasLowResWebp(){
        return lowResWebpFN != null;
    }

  /*  public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
        if(url == null) url = originalUrl;
    }

    public boolean isWaterMarked(){
        return !originalUrl.equals(url);
    }

    public String getLowResWebpUrl() {
        return lowResWebpUrl;
    }

    public void setLowResWebpUrl(String lowResWebpUrl) {
        this.lowResWebpUrl = lowResWebpUrl;
    }
*/
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

    public String getWaterMarkedFN() {
        return waterMarkedFN;
    }

    public void setWaterMarkedFN(String waterMarkedFN) {
        this.waterMarkedFN = waterMarkedFN;
    }

    public List<String> getSearchTags() {
        return searchTags;
    }

    public void setSearchTags(List<String> searchTags) {
        this.searchTags = searchTags;
    }
}

/*case class GifMetaData(@Key("_id") _id: String, url: String, publishedAt: Date, tags: List[String],
                       categories: List[String], title: String, idiom: String, size: Long, sha256: String,
                       source:Option[String], publishedBy:String, sourceUrl: Option[String], public: Boolean) {

  def newCategories(newCategories: List[String]) = GifMetaData( _id, url, publishedAt, tags,
    newCategories, title, idiom, size, sha256, source,publishedBy, sourceUrl, public)

  def newTags(newTags: List[String]) = GifMetaData(_id, url, publishedAt, newTags,
    categories, title, idiom, size, sha256, source, publishedBy, sourceUrl, public)
}*/
