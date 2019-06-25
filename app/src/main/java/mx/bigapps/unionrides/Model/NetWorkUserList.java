package mx.bigapps.unionrides.Model;

/**
 * Created by admin on 09-11-2017.
 */
public class NetWorkUserList {
    String user_id, fullname, nickname, Request_sent, user_status, type, profileimage;

    public NetWorkUserList(String user_id, String fullname, String nickname, String Request_sent, String user_status, String type, String profileimage) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.nickname = nickname;
        this.user_status = user_status;
        this.Request_sent = Request_sent;
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

    public String getRequest_sent() {
        return Request_sent;
    }

    public void setRequest_sent(String email) {
        this.Request_sent = email;
    }

    public String getuser_status() {
        return user_status;
    }

    public void setuser_status(String veryfycode) {
        this.user_status = veryfycode;
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
