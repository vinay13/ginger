package dto;

import model.UserRole;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by senthil
 */
public class SignInResponse {

    private String token;
    private Set<UserRole> role = new HashSet<>();

    public SignInResponse(String token) {
        this.token = token;
    }

    public SignInResponse(String token, Set<UserRole> role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<UserRole> getRole() {
        return role;
    }

    public void setRole(Set<UserRole> role) {
        this.role = role;
    }
}
