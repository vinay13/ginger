package model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by senthil
 */
public class GifCensorAudit {

    @Id
    private String id;
    private String gifId;
    private String reason;
    private String actionBy;
    private Date timeStamp;
    private boolean enable;

    public GifCensorAudit() {
    }

    public GifCensorAudit(String gifId, String reason, String actionBy, boolean enable) {
        this.gifId = gifId;
        this.reason = reason;
        this.actionBy = actionBy;
        this.enable = enable;
        this.timeStamp = new Date();
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
