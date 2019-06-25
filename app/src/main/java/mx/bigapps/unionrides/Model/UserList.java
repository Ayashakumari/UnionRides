package mx.bigapps.unionrides.Model;

/**
 * Created by admin on 09-11-2017.
 */
public class UserList {
    String user_id,fullname,nickname,email,veryfycode,type,profileimage;

    public UserList(String user_id, String fullname, String nickname, String email, String veryfycode, String type, String profileimage) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.nickname = nickname;
        this.veryfycode = veryfycode;
        this.email = email;
        this.type = type;
        this.profileimage = profileimage;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVeryfycode() {
        return veryfycode;
    }

    public void setVeryfycode(String veryfycode) {
        this.veryfycode = veryfycode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
