package dto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by senthil
 */
public class GifCountStats {

    private List<GifIdiomStat> idiomStats = new LinkedList<GifIdiomStat>();

    private Long totalCount;

    public GifCountStats() {
    }

    public GifCountStats(List<GifIdiomStat> idiomStats, Long totalCount) {
        this.idiomStats = idiomStats;
        this.totalCount = totalCount;
    }

    public List<GifIdiomStat> getIdiomStats() {
        return idiomStats;
    }

    public void setIdiomStats(List<GifIdiomStat> idiomStats) {
        this.idiomStats = idiomStats;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
