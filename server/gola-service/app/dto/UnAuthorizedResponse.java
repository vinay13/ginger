package dto;

/**
 * Created by senthil
 */
public class UnAuthorizedResponse {
    private boolean inactive;
    private boolean locked;

    public UnAuthorizedResponse(boolean inactive, boolean locked) {
        this.inactive = inactive;
        this.locked = locked;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
