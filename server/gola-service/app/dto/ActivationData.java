package dto;

/**
 * Created by senthil
 */
public class ActivationData {
    private String activationUrl;
    private String password;

    public ActivationData(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
