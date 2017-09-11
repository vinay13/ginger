package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Welcome on 11-Aug-2017.
 */
public class GifTagCountDetail {

    private List<GifTagDetail> tagDetails = new LinkedList<GifTagDetail>();

    private Long totalCount;

    public GifTagCountDetail(List<GifTagDetail> tagDetails, Long totalCount) {
        this.tagDetails = tagDetails;
        this.totalCount = totalCount;
    }

    public List<GifTagDetail> getTagDetails() {
        return tagDetails;
    }

    public void setTagDetails(List<GifTagDetail> tagDetails) {
        this.tagDetails = tagDetails;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}

