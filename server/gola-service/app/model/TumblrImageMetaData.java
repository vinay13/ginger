package model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class TumblrImageMetaData {

    @Id
    private String id;

    private String blogUrl;
    private String photoUrl;
    private List<String> tags = new LinkedList<>();

    private List<Idiom> idioms = new LinkedList<>();

    private Date requestedDate;
    private String requestedBy;
    private Date completedDate;
    private UploadRequestStatus uploadRequestStatus;

    public TumblrImageMetaData(String blogUrl, String photoUrl, List<String> tags) {
        this.blogUrl = blogUrl;
        this.photoUrl = photoUrl;
        this.tags = tags;
    }

    public TumblrImageMetaData() {
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public UploadRequestStatus getUploadRequestStatus() {
        return uploadRequestStatus;
    }

    public void setUploadRequestStatus(UploadRequestStatus uploadRequestStatus) {
        this.uploadRequestStatus = uploadRequestStatus;
    }

    @Override
    public String toString() {
        return "TumblrImageMetaData{" +
                "blogUrl='" + blogUrl + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", tags=" + tags +
                ", requestedDate=" + requestedDate +
                ", requestedBy='" + requestedBy + '\'' +
                ", completedDate=" + completedDate +
                ", uploadRequestStatus=" + uploadRequestStatus +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Idiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<Idiom> idioms) {
        this.idioms = idioms;
    }
}
