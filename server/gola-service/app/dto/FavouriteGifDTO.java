package dto;

import model.GifMetaData;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by senthil
 */
public class FavouriteGifDTO {

    private String emailId;
    private Set<GifMetaData> favouriteGifs = new HashSet<GifMetaData>();

    public FavouriteGifDTO() {
    }

    public FavouriteGifDTO(String emailId, Set<GifMetaData> favouriteGifs) {
        this.emailId = emailId;
        this.favouriteGifs = favouriteGifs;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Set<GifMetaData> getFavouriteGifs() {
        return favouriteGifs;
    }

    public void setFavouriteGifs(Set<GifMetaData> favouriteGifs) {
        this.favouriteGifs = favouriteGifs;
    }
}
