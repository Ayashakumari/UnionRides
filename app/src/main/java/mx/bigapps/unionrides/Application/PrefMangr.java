package mx.bigapps.unionrides.Application;

import android.content.Context;

import mx.bigapps.unionrides.utils.AppConstants;


public class PrefMangr {

    static PrefMangr preferenceInstance;

    private PrefMangr(Context context) {
        super();
    }

    public static void initInstance() {
        if (preferenceInstance == null)
            preferenceInstance = new PrefMangr(AppController.mContext);
    }

    public static PrefMangr getInstance() {
        return preferenceInstance;
    }


    public String getDeviceId() {
        return AppController.getSharedPreferences().getString(AppConstants.Regid, null);
    }


    public void setDeviceId(String deviceId) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Regid, deviceId).commit();
    }


    public String getVideoUrl() {
        return AppController.getSharedPreferences().getString(AppConstants.Cover_video, null);
    }

    public void setVideoUrl(String videoUrl) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Cover_video, videoUrl).commit();
    }

    public String getCoverPicID() {
        return AppController.getSharedPreferences().getString(AppConstants.Cover_Pic_ID, null);
    }

    public void setCoverPicID(String coverPicID) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Cover_Pic_ID, coverPicID).commit();
    }

    public String getFullName() {
        return AppController.getSharedPreferences().getString(AppConstants.FullName, null);
    }

    public void setFullName(String userdob) {
        AppController.getSharedPreferences().edit().putString(AppConstants.FullName, userdob).commit();
    }

    public String getCoverID() {
        return AppController.getSharedPreferences().getString(AppConstants.CoverID, null);
    }

    public void setCoverID(String id) {
        AppController.getSharedPreferences().edit().putString(AppConstants.CoverID, id).commit();
    }


    public String getUsernickName() {
        return AppController.getSharedPreferences().getString(AppConstants.USERFNAME, null);
    }

    public void setUsernickName(String userfName) {
        AppController.getSharedPreferences().edit().putString(AppConstants.USERFNAME, userfName).commit();
    }

    public String getinvitefriendId() {
        return AppController.getSharedPreferences().getString(AppConstants.invitefriendId, null);
    }

    public void setinvitefriendId(String invitefriendId) {
        AppController.getSharedPreferences().edit().putString(AppConstants.invitefriendId, invitefriendId).commit();
    }

    public String getUserLName() {
        return AppController.getSharedPreferences().getString(AppConstants.USERLNAME, null);
    }

    public void setUserLName(String userlName) {
        AppController.getSharedPreferences().edit().putString(AppConstants.USERLNAME, userlName).commit();
    }

    public String getUserName() {
        return AppController.getSharedPreferences().getString(AppConstants.USERNAME, null);
    }

    public void setUserName(String userName) {
        AppController.getSharedPreferences().edit().putString(AppConstants.USERNAME, userName).commit();
    }

    public String getuserMobileNO() {
        return AppController.getSharedPreferences().getString(AppConstants.MOBILE_NO, null);
    }

    public void setuserMobileNO(String Mobileno) {
        AppController.getSharedPreferences().edit().putString(AppConstants.MOBILE_NO, Mobileno).commit();
    }

    public String getUseremail() {
        return AppController.getSharedPreferences().getString(AppConstants.EMAIL, null);
    }

    public void setUseremail(String useremail) {
        AppController.getSharedPreferences().edit().putString(AppConstants.EMAIL, useremail).commit();
    }


    public String getLocation() {
        return AppController.getSharedPreferences().getString(AppConstants.Location, null);
    }

    public void setLocation(String albumId) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Location, albumId).commit();
    }

    public String getPassword() {
        return AppController.getSharedPreferences().getString(AppConstants.PASSWORD, null);
    }

    public void setPassword(String password) {
        AppController.getSharedPreferences().edit().putString(AppConstants.PASSWORD, password).commit();
    }

    public String getUserId() {
        return AppController.getSharedPreferences().getString(AppConstants.USERID, null);
    }

    public void setUSerId(String uSerId) {
        AppController.getSharedPreferences().edit().putString(AppConstants.USERID, uSerId).commit();
    }



    public String getuserType() {
        return AppController.getSharedPreferences().getString(AppConstants.userType, null);
    }

    public void setuserType(String userType) {
        AppController.getSharedPreferences().edit().putString(AppConstants.userType, userType).commit();
    }

    public String getDeliveryphone() {
        return AppController.getSharedPreferences().getString(AppConstants.Delivryaphone, null);
    }

    public void setDeliveryphone(String Delivryaphone) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Delivryaphone, Delivryaphone).commit();
    }

    public String getDeliveryemail() {
        return AppController.getSharedPreferences().getString(AppConstants.Delivryemail, null);
    }

    public void setDeliveryemail(String Delivryemail) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Delivryemail, Delivryemail).commit();
    }

    public String getDeliveryAddress() {
        return AppController.getSharedPreferences().getString(AppConstants.DelivryfullAddress, null);
    }

    public void setDeliveryAddress(String DelivryfullAddress) {
        AppController.getSharedPreferences().edit().putString(AppConstants.DelivryfullAddress, DelivryfullAddress).commit();
    }

    public String getcount() {
        return AppController.getSharedPreferences().getString(AppConstants.count, null);
    }

    public void setcount(String count) {
        AppController.getSharedPreferences().edit().putString(AppConstants.count, count).commit();
    }

    public String getremberedEmail() {
        return AppController.getSharedPreferences().getString(AppConstants.RememberedEmail, null);
    }

    public void setrememberedEmail(String email) {
        AppController.getSharedPreferences().edit().putString(AppConstants.RememberedEmail, email).commit();
    }

    public String getremberedPassword() {
        return AppController.getSharedPreferences().getString(AppConstants.RememberedPassword, null);
    }

    public void setrememberedPassword(String password) {
        AppController.getSharedPreferences().edit().putString(AppConstants.RememberedPassword, password).commit();
    }

    public String getfacebook() {
        return AppController.getSharedPreferences().getString(AppConstants.facebook, null);
    }

    public void setfacebook(String facebook) {
        AppController.getSharedPreferences().edit().putString(AppConstants.facebook, facebook).commit();
    }


    public String getProfilepic() {
        return AppController.getSharedPreferences().getString(AppConstants.ProfilePic, null);
    }

    public void setProfilepic(String pic) {
        AppController.getSharedPreferences().edit().putString(AppConstants.ProfilePic, pic).commit();
    }

    public String getlatitude() {
        return AppController.getSharedPreferences().getString(AppConstants.Latitude, null);
    }

    public void setlatitude(String Latitude) {
        AppController.getSharedPreferences().edit().putString(AppConstants.Latitude, Latitude).commit();
    }

    public String getlogitude() {
        return AppController.getSharedPreferences().getString(AppConstants.langitude, null);
    }

    public void setlogitude(String logitude) {
        AppController.getSharedPreferences().edit().putString(AppConstants.langitude, logitude).commit();
    }



}
