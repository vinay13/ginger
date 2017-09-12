package dto;

import model.Idiom;
import model.TagUpdateAudit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Welcome on 11-Aug-2017.
 */
public class GifTagDetail {

    private String gifId;
    private String url;
    private List<String> tags = new LinkedList<String>();
    private List<String> addedTags=new LinkedList<>();
    private List<String> removedTags=new LinkedList<>();
    private List<Idiom> idioms=new LinkedList<>();
    private int size;

    public GifTagDetail(String gifId, String url, List<String> tags,List<Idiom> idioms,int size) {
        this.gifId = gifId;
        this.url = url;
        this.tags = tags;
        this.idioms=idioms;
        this.size=size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<String> getAddedTags() {
        return addedTags;
    }

    public void setAddedTags(List<String> addedTags) {
        this.addedTags = addedTags;
    }

    public List<String> getRemovedTags() {
        return removedTags;
    }

    public void setRemovedTags(List<String> removedTags) {
        this.removedTags = removedTags;
    }
}
