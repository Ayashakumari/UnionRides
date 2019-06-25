package mx.bigapps.unionrides.Model;

/**
 * Created by admin on 14-11-2017.
 */
public class Comments {
    String fullname, profileimage, commentsid, comments, datetime;

    public Comments(String fullname, String profileimage, String commentsid, String comments, String datetime) {
        this.fullname = fullname;
        this.profileimage = profileimage;
        this.commentsid = commentsid;
        this.comments = comments;
        this.datetime = datetime;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getCommentsid() {
        return commentsid;
    }

    public void setCommentsid(String commentsid) {
        this.commentsid = commentsid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
