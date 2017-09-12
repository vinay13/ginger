package model;

/**
 * Created by senthil
 */
public class UserSecurity {

    private String secId;
    private String secKey;

    public UserSecurity() {
    }

    public UserSecurity(String secId, String secKey) {
        this.secId = secId;
        this.secKey = secKey;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }
}
