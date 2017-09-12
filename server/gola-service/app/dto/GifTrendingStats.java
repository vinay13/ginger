package dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by senthil
 */
public class GifTrendingStats {

    private List<GifIdiomTrendingStats> gifIdiomTrendingStatsList = new LinkedList<>();

    public GifTrendingStats() {
    }

    public GifTrendingStats(List<GifIdiomTrendingStats> gifIdiomTrendingStatsList) {
        this.gifIdiomTrendingStatsList = gifIdiomTrendingStatsList;
    }

    public List<GifIdiomTrendingStats> getGifIdiomTrendingStatsList() {
        return gifIdiomTrendingStatsList;
    }

    public void setGifIdiomTrendingStatsList(List<GifIdiomTrendingStats> gifIdiomTrendingStatsList) {
        this.gifIdiomTrendingStatsList = gifIdiomTrendingStatsList;
    }


}
