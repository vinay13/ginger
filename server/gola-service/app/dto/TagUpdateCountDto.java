package dto;

/**
 * Created by senthil
 */
public class TagUpdateCountDto {

    private Integer count;
    private String userId;

    public TagUpdateCountDto(Integer count, String userId) {
        this.count = count;
        this.userId = userId;
    }

    public TagUpdateCountDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
