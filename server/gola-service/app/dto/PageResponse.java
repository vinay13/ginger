package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class PageResponse<I> {

    private long totalCount;
    private List<I> contents = new LinkedList<>();

    public PageResponse(long totalCount, List<I> contents) {
        this.totalCount = totalCount;
        this.contents = contents;
    }

    public PageResponse() {
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<I> getContents() {
        return contents;
    }

    public void setContents(List<I> contents) {
        this.contents = contents;
    }
}
