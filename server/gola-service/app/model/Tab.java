package model;

import dto.TrendingPeriod;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class Tab {

    private String id;
    private String name;
    private List<String> tags = new LinkedList<String>();
    private boolean curated = false;
    private boolean trending = false;
    private boolean search = false;
    private short order;
    private TrendingPeriod trendingPeriod;

    public Tab(String name, List<String> tags, boolean curated, boolean trending, short order, boolean search, TrendingPeriod trendingPeriod) {
        this.name = name;
        this.tags = tags;
        this.curated = curated;
        this.trending = trending;
        this.order = order;
        this.search = search;
        this.trendingPeriod = trendingPeriod;
    }

    public Tab() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isCurated() {
        return curated;
    }

    public void setCurated(boolean curated) {
        this.curated = curated;
    }

    public boolean isTrending() {
        return trending;
    }

    public void setTrending(boolean trending) {
        this.trending = trending;
    }

    public short getOrder() {
        return order;
    }

    public void setOrder(short order) {
        this.order = order;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public TrendingPeriod getTrendingPeriod() {
        return trendingPeriod;
    }

    public void setTrendingPeriod(TrendingPeriod trendingPeriod) {
        this.trendingPeriod = trendingPeriod;
    }
}
