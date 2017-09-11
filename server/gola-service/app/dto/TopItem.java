package dto;

/**
 * Created by senthil
 */
public class TopItem {
    private String text;
    private String displayName;
    private String exclude;
    private String hitCount;
    private String gifId;
    //private String url;
    /*private String thumbNailUrl;
    private String lowResUrl;
    private String lowResWebpUrl;*/

    private String baseUrl;
    private String originalFN;
    private String lowResFN;
    private String lowResWebpFN;
    private String thumbNailFN;
    private String waterMarkedFN;

    private boolean active = true;
    private int width;
    private int height;
    private int lowResWidth;
    private int lowResHeight;
    private String triggerExpr;
    private int order;

    public TopItem(String text) {
        this.text = text;
    }

    public TopItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHitCount() {
        return hitCount;
    }

    public void setHitCount(String hitCount) {
        this.hitCount = hitCount;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    /*public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }*/

    /*@Override
    public String toString() {
        return "TopItem{" +
                "text='" + text + '\'' +
                ", hitCount='" + hitCount + '\'' +
                ", gifId='" + gifId + '\'' +
                ", url='" + url + '\'' +
                ", thumbNailUrl='" + thumbNailUrl + '\'' +
                ", lowResUrl='" + lowResUrl + '\'' +
                ", active=" + active +
                ", width=" + width +
                ", height=" + height +
                ", triggerExpr='" + triggerExpr + '\'' +
                '}';
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getLowResUrl() {
        return lowResUrl;
    }

    public void setLowResUrl(String lowResUrl) {
        this.lowResUrl = lowResUrl;
    }
*/
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

    public String getTriggerExpr() {
        return triggerExpr;
    }

    public void setTriggerExpr(String triggerExpr) {
        this.triggerExpr = triggerExpr;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

  /*  public String getLowResWebpUrl() {
        return lowResWebpUrl;
    }

    public void setLowResWebpUrl(String lowResWebpUrl) {
        this.lowResWebpUrl = lowResWebpUrl;
    }
*/
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
}
