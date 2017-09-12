package model;

import java.util.Date;

/**
 * Created by senthil
 */
public class LowResGifGenerationFailure {

    private String id;
    private String gifId;

    private Date attemptedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public Date getAttemptedDate() {
        return attemptedDate;
    }

    public void setAttemptedDate(Date attemptedDate) {
        this.attemptedDate = attemptedDate;
    }
}
