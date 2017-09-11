package model;

import org.springframework.data.annotation.Id;

/**
 * Created by senthil
 */
public class UserProfile {

    @Id
    private String id;
    private String userName;
    private String fullName;
    private String webLink;

    public UserProfile() {
    }

    public UserProfile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}

/*case class UserProfile(@Key("_id") _id: String, userName: String, fullName: String, webLink: String){
  def withEmailId(emailId:String) = UserProfile(emailId, userName, fullName, webLink)
  def this(id: String) = this(id, null, null, null)
}*/
