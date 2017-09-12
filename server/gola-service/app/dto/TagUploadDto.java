package dto;

/**
 * Created by venkat
 */
public class TagUploadDto {

    private String text;
    private String displayName;
    private String exclude;
    private int order;

    public TagUploadDto(String text, String displayName, String exclude) {
        this.text = text;
        this.displayName = displayName;
        this.exclude = exclude;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
