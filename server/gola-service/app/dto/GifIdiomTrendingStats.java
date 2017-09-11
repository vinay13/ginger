package dto;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by senthil
 */
public class GifIdiomTrendingStats {

    private String idiom;
    private Map<TrendingPeriod, List<TopItem>> topSearches = new HashMap<TrendingPeriod, List<TopItem>>();
    private Map<TrendingPeriod, List<TopItem>> topShares = new HashMap<TrendingPeriod, List<TopItem>>();

    public GifIdiomTrendingStats(String idiom, Map<TrendingPeriod, List<TopItem>> topSearches, Map<TrendingPeriod, List<TopItem>> topShares) {
        this.idiom = idiom;
        this.topSearches = topSearches;
        this.topShares = topShares;
    }

    public GifIdiomTrendingStats() {
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public Map<TrendingPeriod, List<TopItem>> getTopSearches() {
        return topSearches;
    }

    public void setTopSearches(Map<TrendingPeriod, List<TopItem>> topSearches) {
        this.topSearches = topSearches;
    }

    public Map<TrendingPeriod, List<TopItem>> getTopShares() {
        return topShares;
    }

    public void setTopShares(Map<TrendingPeriod, List<TopItem>> topShares) {
        this.topShares = topShares;
    }
}
