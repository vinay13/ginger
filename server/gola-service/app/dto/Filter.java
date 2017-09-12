package dto;

import java.util.concurrent.TimeUnit;

/**
 * Created by senthil
 */
public class Filter {

    private String idiom;
    private String tag;
    private TrendingPeriod trendingPeriod;

    public Filter() {
    }

    public Filter(String idiom, String tag, TrendingPeriod trendingPeriod) {
        this.idiom = idiom;
        this.tag = tag;
        this.trendingPeriod = trendingPeriod;
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TrendingPeriod getTrendingPeriod() {
        return trendingPeriod;
    }

    public void setTrendingPeriod(TrendingPeriod trendingPeriod) {
        this.trendingPeriod = trendingPeriod;
    }
}
