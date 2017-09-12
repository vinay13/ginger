package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by senthil
 */
public class BulkUploadRequest {

    @Id
    private String path;
    private List<Idiom> idioms;
    private String requestedBy;

    @DBRef
    private List<RequestedFile> files = new LinkedList<>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<RequestedFile> getFiles() {
        return files;
    }

    public void setFiles(List<RequestedFile> files) {
        this.files = files;
    }

    public List<Idiom> getIdioms() {
        return idioms;
    }

    public void setIdioms(List<Idiom> idioms) {
        this.idioms = idioms;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
