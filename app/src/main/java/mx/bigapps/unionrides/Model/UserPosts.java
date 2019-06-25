package mx.bigapps.unionrides.Model;

/**
 * Created by admin on 09-11-2017.
 */
public class UserPosts {
    String postid, userid, posttitle, data, videoThumbnail, parentId, posttype, postfrom, commentCount, likeCount,
            flagged, posted_time, videoThumb, myPost, posted_by, profile_image, commentsende, comments, cmntdatetime,
            imagethumb640, imagethumb320,like_status,postTitle,video_web_views_url,sponserimage,image_web_views_url;

    public UserPosts(String postid, String userid, String posttitle, String data, String videoThumbnail, String parentId, String flagged, String posttype, String postfrom, String commentCount, String rideCount, String posted_time, String videoThumb, String myPost, String posted_by, String profile_image) {
        this.postid = postid;
        this.userid = userid;
        this.posttitle = posttitle;
        this.data = data;
        this.videoThumbnail = videoThumbnail;
        this.parentId = parentId;
        this.posttype = posttype;
        this.postfrom = postfrom;
        this.commentCount = commentCount;
        this.likeCount = rideCount;
        this.flagged = flagged;
        this.posted_time = posted_time;
        this.videoThumb = videoThumb;
        this.myPost = myPost;
        this.posted_by = posted_by;
        this.profile_image = profile_image;
    }

    public UserPosts(String postid, String userid, String posttitle, String data, String videoThumbnail, String parentId, String flagged, String posttype, String postfrom, String commentCount, String likeCount, String posted_time, String videoThumb, String myPost, String posted_by, String profile_image, String commentsende, String comments, String cmntdatetime, String imagethumb640, String imagethumb320,String like_status,String postTitle,String video_web_views_url,String sponserimage,String image_web_views_url) {
        this.postid = postid;
        this.userid = userid;
        this.posttitle = posttitle;
        this.data = data;
        this.videoThumbnail = videoThumbnail;
        this.parentId = parentId;
        this.posttype = posttype;
        this.postfrom = postfrom;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.flagged = flagged;
        this.posted_time = posted_time;
        this.videoThumb = videoThumb;
        this.myPost = myPost;
        this.posted_by = posted_by;
        this.profile_image = profile_image;
        this.commentsende = commentsende;
        this.comments = comments;
        this.cmntdatetime = cmntdatetime;
        this.imagethumb640 = imagethumb640;
        this.imagethumb320 = imagethumb320;
        this.like_status=like_status;
        this.postTitle=postTitle;
        this.video_web_views_url=video_web_views_url;
        this.sponserimage=sponserimage;
        this.image_web_views_url=image_web_views_url;

    }

    public String getImage_web_views_url() {
        return image_web_views_url;
    }

    public void setImage_web_views_url(String image_web_views_url) {
        this.image_web_views_url = image_web_views_url;
    }

    public String getSponserimage() {
        return sponserimage;
    }

    public void setSponserimage(String sponserimage) {
        this.sponserimage = sponserimage;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getVideo_web_views_url() {
        return video_web_views_url;
    }

    public void setVideo_web_views_url(String video_web_views_url) {
        this.video_web_views_url = video_web_views_url;
    }

    public String getPostcaption() {
        return postTitle;
    }

    public void setPostcaption(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getImagethumb640() {
        return imagethumb640;
    }

    public void setImagethumb640(String imagethumb640) {
        this.imagethumb640 = imagethumb640;
    }

    public String getImagethumb320() {
        return imagethumb320;
    }

    public void setImagethumb320(String imagethumb320) {
        this.imagethumb320 = imagethumb320;
    }

    public String getCmntdatetime() {
        return cmntdatetime;
    }

    public void setCmntdatetime(String cmntdatetime) {
        this.cmntdatetime = cmntdatetime;
    }

    public String getCommentsende() {
        return commentsende;
    }

    public void setCommentsende(String commentsende) {
        this.commentsende = commentsende;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getMyPost() {
        return myPost;
    }

    public void setMyPost(String myPost) {
        this.myPost = myPost;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    public String getPosted_time() {
        return posted_time;
    }

    public void setPosted_time(String posted_time) {
        this.posted_time = posted_time;
    }

    public String getFlagged() {
        return flagged;
    }

    public void setFlagged(String flagged) {
        this.flagged = flagged;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getPostfrom() {
        return postfrom;
    }

    public void setPostfrom(String postfrom) {
        this.postfrom = postfrom;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getlikeCount() {
        return likeCount;
    }

    public void setlikeCount(String rideCount) {
        this.likeCount = rideCount;
    }


}
