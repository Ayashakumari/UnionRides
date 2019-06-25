
package mx.bigapps.unionrides.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ride_id")
    @Expose
    private String rideId;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("comment_by")
    @Expose
    private String commentBy;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Response() {
    }

    /**
     * 
     * @param userId
     * @param profilePic
     * @param datetime
     * @param comments
     * @param commentId
     * @param commentBy
     * @param rideId
     */
    public Response(String commentId, String userId, String rideId, String comments, String datetime, String profilePic, String commentBy) {
        super();
        this.commentId = commentId;
        this.userId = userId;
        this.rideId = rideId;
        this.comments = comments;
        this.datetime = datetime;
        this.profilePic = profilePic;
        this.commentBy = commentBy;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }



}
