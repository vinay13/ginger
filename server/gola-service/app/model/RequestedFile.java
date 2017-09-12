package model;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class RequestedFile {

    @Id
    private String id;
    private String path;
    private UploadRequestStatus uploadRequestStatus;
    private Date requestedDate;
    private Date completedDate;
    private String source;
    private List<String> tags = new LinkedList<String>();
    private List<Idiom> idioms = new LinkedList<>();


    public RequestedFile(String path, UploadRequestStatus uploadRequestStatus, Date requestedDate, String source) {
        this.path = path;
        this.uploadRequestStatus = uploadRequestStatus;
        this.requestedDate = requestedDate;
        this.source = source;
    }

    public RequestedFile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UploadRequestStatus getUploadRequestStatus() {
        return uploadRequestStatus;
    }

    public void setUploadRequestStatus(UploadRequestStatus uploadRequestStatus) {
        this.uploadRequestStatus = uploadRequestStatus;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
}
