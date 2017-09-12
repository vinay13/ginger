package model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by senthil
 */

public class User {

    @Id
    private String id;
    private String deviceId;

    @Indexed(unique = true)
    private String emailId;
    private String password;
    private String partnerId;
    private UserSecurity userSecurity;
    private String oauthCode;
    private UserSource userSource;
    private Set<UserRole> userRoles = new HashSet<>();
    private Device device;


    private boolean active;


    public User(String deviceId, String emailId, String password, String partnerId, UserSecurity userSecurity) {
        this.deviceId = deviceId;
        this.emailId = emailId;
        this.password = password;
        this.partnerId = partnerId;
        this.userSecurity = userSecurity;
    }

    public User() {
    }

    public String getOauthCode() {
        return oauthCode;
    }

    public void setOauthCode(String oauthCode) {
        this.oauthCode = oauthCode;
    }

    public UserSource getUserSource() {
        return userSource;
    }

    public void setUserSource(UserSource userSource) {
        this.userSource = userSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public UserSecurity getUserSecurity() {
        return userSecurity;
    }

    public void setUserSecurity(UserSecurity userSecurity) {
        this.userSecurity = userSecurity;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRole) {
        this.userRoles = userRoles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}


