package mx.bigapps.unionrides.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import mx.bigapps.unionrides.Application.PrefMangr;


@SuppressLint("NewApi")
public class LoadApi {

    Activity activity;

    Bitmap bitmap = null;

    static JSONObject json = null;
    String result;
    String image;
    String show;
    JSONObject result1;


    String company_name;
    String num;

    public String getReceiver_status_online_offline() {
        return receiver_status_online_offline;
    }

    public void setReceiver_status_online_offline(String receiver_status_online_offline) {
        this.receiver_status_online_offline = receiver_status_online_offline;
    }

    public JSONObject getCompany_details() {
        return company_details;
    }

    public void setCompany_details(JSONObject company_details) {
        this.company_details = company_details;
    }

    JSONObject company_details;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;
    String auto_reply_message_doctor;
    String receiver_status_online_offline, support_online_status;

    public String getSupport_online_status() {
        return support_online_status;
    }

    public void setSupport_online_status(String support_online_status) {
        this.support_online_status = support_online_status;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAuto_reply_message_doctor() {
        return auto_reply_message_doctor;
    }

    public void setAuto_reply_message_doctor(String auto_reply_message_doctor) {
        this.auto_reply_message_doctor = auto_reply_message_doctor;
    }

    public String getChat_seeting_doctor() {
        return chat_seeting_doctor;
    }

    public void setChat_seeting_doctor(String chat_seeting_doctor) {
        this.chat_seeting_doctor = chat_seeting_doctor;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getResult() {
        return result;
    }

    public void setResult1(JSONObject result1) {
        this.result1 = result1;
    }

    public JSONObject getResult1() {
        return result1;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image_id, chat_seeting_doctor;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public JSONObject Action_profileSetting_Image(String action, String user_id,
                                                  byte[] profile_pic, String posttitle) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 150000);  // allow 5 seconds to create the server connection
            HttpConnectionParams.setSoTimeout(httpParameters, 150000);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (profile_pic.length > 0) {


                entity.addPart("image", new ByteArrayBody(profile_pic, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + profile_pic);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("post_title", new StringBody(posttitle, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    Log.d("msgexp", " " + e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                Log.d("msgexp1", " " + e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("msgexp2", " " + e);
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_AddEvent(String Addevent, String userId, String title, String starttime, String startdate, String endtime, String enddate, String eventlocation, String latitude, String longitude, String profiletype, String description, String friendid, byte[] profile_pic) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);  // allow 5 seconds to create the server connection
            HttpConnectionParams.setSoTimeout(httpParameters, 60000);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            // and another 5 seconds to retreive the data


            MultipartEntity entity = new MultipartEntity();


            if (profile_pic.length > 0) {


                entity.addPart("image", new ByteArrayBody(profile_pic, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + profile_pic);

            }
           /* user_id , event_title ,start_date, start_time , end_date, end_time
            event_location, latitude, longitude , publice , description , image , invite_friend*/
            entity.addPart("user_id", new StringBody(userId, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody("Addevent", "text/plain", Charset.forName("UTF-8")));
            entity.addPart("event_title", new StringBody(title, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("start_date", new StringBody(startdate, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("start_time", new StringBody(starttime, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("end_date", new StringBody(enddate, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("end_time", new StringBody(endtime, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("event_location", new StringBody(eventlocation, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("latitude", new StringBody(latitude, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("longitude", new StringBody(longitude, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("public", new StringBody(profiletype, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("description", new StringBody(description, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("invite_friend", new StringBody(friendid, "text/plain", Charset.forName("UTF-8")));
                /*entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));*/
            Log.d("entity", String.valueOf(entity));

            int statusCode = -1;


            try {

                httppost.setEntity(entity);

                HttpResponse resp = httpclient.execute(httppost);

                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_EditEvent(String editevent, String eventId, String userId, String title, String starttime, String startdate, String endtime, String enddate, String eventlocation, String latitude, String longitude, String profiletype, String description, String friendid, byte[] profile_pic) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (profile_pic.length > 0) {


                entity.addPart("image", new ByteArrayBody(profile_pic, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + profile_pic);

            }
           /* user_id , event_title ,start_date, start_time , end_date, end_time
            event_location, latitude, longitude , publice , description , image , invite_friend*/
            entity.addPart("event_id", new StringBody(eventId, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("user_id", new StringBody(userId, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody("Editevent", "text/plain", Charset.forName("UTF-8")));
            entity.addPart("event_title", new StringBody(title, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("start_date", new StringBody(startdate, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("start_time", new StringBody(starttime, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("end_date", new StringBody(enddate, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("end_time", new StringBody(endtime, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("event_location", new StringBody(eventlocation, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("latitude", new StringBody(latitude, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("longitude", new StringBody(longitude, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("public", new StringBody(profiletype, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("description", new StringBody(description, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("invite_friend", new StringBody(friendid, "text/plain", Charset.forName("UTF-8")));
                /*entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));*/
            Log.d("entity", String.valueOf(entity));

            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_profileSetting(String action, String full_name, String nick_name, String user_id, String user_type, String interest_in,
                                            byte[] profile_pic) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (profile_pic.length > 0) {


                entity.addPart("profile_image", new ByteArrayBody(profile_pic, "image/jpeg", "test1.jpg"));
                //Log.d("55555",""+profile_pic);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            Log.d("user_id", "<>" + user_id.toString());
            entity.addPart("full_name", new StringBody(full_name, "text/plain", Charset.forName("UTF-8")));
            Log.d("ful_name", "<><>" + full_name.toString());
            entity.addPart("nick_name", new StringBody(nick_name, "text/plain", Charset.forName("UTF-8")));
            Log.d("nick", "<><><>" + nick_name.toString());
            entity.addPart("user_type", new StringBody(user_type, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));
            //   entity.addPart(" interest_in", new StringBody(interest_in, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_Awards(String action, String user_id, String award_date, String award_name, byte[] cover_photo_id) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (cover_photo_id.length > 0) {


                entity.addPart("award_picture", new ByteArrayBody(cover_photo_id, "image/jpeg", "test1.jpg"));
                //Log.d("55555",""+profile_pic);

            }
            entity.addPart("cover_photo_id", new StringBody(PrefMangr.getInstance().getCoverPicID(), "text/plain", Charset.forName("UTF-8")));

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            Log.d("user_id", "<>" + user_id.toString());
            entity.addPart("award_name", new StringBody(award_name, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("award_date", new StringBody(award_date, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("action", new StringBody("Addrideaward", "text/plain", Charset.forName("UTF-8")));
            //   entity.addPart(" interest_in", new StringBody(interest_in, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject uploadcover(String action, String user_id, byte[] picture, String abc) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 150000);  // allow 5 seconds to create the server connection
            HttpConnectionParams.setSoTimeout(httpParameters, 150000);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("cover_image", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody("Add_ride_cover_photo", "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            //entity.addPart("last_name", new StringBody(last_name, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("token", new StringBody(token, "text/plain", Charset.forName("UTF-8")));

            //entity.addPart("email_address", new StringBody(email_address, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("company_name", new StringBody(company_name, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("mobile_number", new StringBody(mobile_number, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("phone_number", new StringBody(phone_number, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("Street_number", new StringBody(street_number, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("address", new StringBody(address, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_upload_Image(String action, String user_id, String cover_id, byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("ride_image", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);

            entity.addPart("cover_photo_id", new StringBody(cover_id, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_SpecificationImage(String action, String user_id, String cover_id, String brand_name, String model_name
            , String color, String horsepower, String year, String touque, String mph, byte[] picture, String spec_price) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("spec_picture", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("cover_photo_id", new StringBody(cover_id, "text/plain", Charset.forName("UTF-8")));
            // params.put("action", "Addridespecification");
//    params.put("brand_name",brand_name);
//    params.put("model_name",model_name);
//    params.put("color",color_str);
//    params.put("horsepower",hp_str);
//    params.put("year",year_str);
//    params.put("touque",tourque_str);
//    params.put("mph",speed_str);
//    params.put("user_id", PrefMangr.getInstance().getUserId());
//
//    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());

            entity.addPart("brand_name", new StringBody(brand_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("model_name", new StringBody(model_name, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("color", new StringBody(color, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("horsepower", new StringBody(horsepower, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("year", new StringBody(year, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("touque", new StringBody(touque, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("mph", new StringBody(mph, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("spec_price", new StringBody(spec_price, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_EditSpecificationImage(String action, String user_id, String specification_id, String brand_name, String model_name
            , String color, String horsepower, String year, String touque, String mph, byte[] picture, String spec_price) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("spec_picture", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            // entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("specification_id", new StringBody(specification_id, "text/plain", Charset.forName("UTF-8")));


            entity.addPart("brand_name", new StringBody(brand_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("model_name", new StringBody(model_name, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("color", new StringBody(color, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("horsepower", new StringBody(horsepower, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("year", new StringBody(year, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("touque", new StringBody(touque, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("mph", new StringBody(mph, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("spec_price", new StringBody(spec_price, "text/plain", Charset.forName("UTF-8")));

            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_EditNOSpecificationImage(String action, String user_id, String specification_id, String brand_name, String model_name
            , String color, String horsepower, String year, String touque, String mph, byte[] picture, String spec_price) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            // entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("specification_id", new StringBody(specification_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("spec_price", new StringBody(spec_price, "text/plain", Charset.forName("UTF-8")));


            entity.addPart("brand_name", new StringBody(brand_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("model_name", new StringBody(model_name, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("color", new StringBody(color, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("horsepower", new StringBody(horsepower, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("year", new StringBody(year, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("touque", new StringBody(touque, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("mph", new StringBody(mph, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_SpecificationN0Image(String action, String user_id, String cover_id, String brand_name, String model_name
            , String color, String horsepower, String year, String touque, String mph, byte[] picture, String spec_price) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("cover_photo_id", new StringBody(cover_id, "text/plain", Charset.forName("UTF-8")));
            // params.put("action", "Addridespecification");
//    params.put("brand_name",brand_name);
//    params.put("model_name",model_name);
//    params.put("color",color_str);
//    params.put("horsepower",hp_str);
//    params.put("year",year_str);
//    params.put("touque",tourque_str);
//    params.put("mph",speed_str);
//    params.put("user_id", PrefMangr.getInstance().getUserId());
//
//    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());
            entity.addPart("spec_price", new StringBody(spec_price, "text/plain", Charset.forName("UTF-8")));


            entity.addPart("brand_name", new StringBody(brand_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("model_name", new StringBody(model_name, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("color", new StringBody(color, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("horsepower", new StringBody(horsepower, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("year", new StringBody(year, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("touque", new StringBody(touque, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("mph", new StringBody(mph, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_AwardImage(String action, String user_id, String cover_id, String award_name, String award_date
            , byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("award_picture", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("cover_photo_id", new StringBody(cover_id, "text/plain", Charset.forName("UTF-8")));
            // params.put("action", "Addridespecification");
//    params.put("brand_name",brand_name);
//    params.put("model_name",model_name);
//    params.put("color",color_str);
//    params.put("horsepower",hp_str);
//    params.put("year",year_str);
//    params.put("touque",tourque_str);
//    params.put("mph",speed_str);
//    params.put("user_id", PrefMangr.getInstance().getUserId());
//
//    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());\
//            params.put("user_id", PrefMangr.getInstance().getUserId());
//
//            params.put("cover_photo_id",PrefMangr.getInstance().getCoverPicID());
//            params.put("award_name",name);
//            params.put("award_date",award_date);

            entity.addPart("award_name", new StringBody(award_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("award_date", new StringBody(award_date, "text/plain", Charset.forName("UTF-8")));

            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return json;
    }


    public JSONObject Action_AwardN0Image(String action, String user_id, String cover_id, String award_name, String award_date
            , byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("cover_photo_id", new StringBody(cover_id, "text/plain", Charset.forName("UTF-8")));
            // params.put("action", "Addridespecification");
//    params.put("brand_name",brand_name);
//    params.put("model_name",model_name);
//    params.put("color",color_str);
//    params.put("horsepower",hp_str);
//    params.put("year",year_str);
//    params.put("touque",tourque_str);
//    params.put("mph",speed_str);
//    params.put("user_id", PrefMangr.getInstance().getUserId());
//
//    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());\
//            params.put("user_id", PrefMangr.getInstance().getUserId());
//
//            params.put("cover_photo_id",PrefMangr.getInstance().getCoverPicID());
//            params.put("award_name",name);
//            params.put("award_date",award_date);

            entity.addPart("award_name", new StringBody(award_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("award_date", new StringBody(award_date, "text/plain", Charset.forName("UTF-8")));

            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    //Edit Award api

    public JSONObject Action_AwardEditImage(String action, String award_id, String award_name, String award_date
            , byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture != null) {


                entity.addPart("award_picture", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + picture);

            }

            //   entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("award_id", new StringBody(award_id, "text/plain", Charset.forName("UTF-8")));
            // params.put("action", "Addridespecification");
//    params.put("brand_name",brand_name);
//    params.put("model_name",model_name);
//    params.put("color",color_str);
//    params.put("horsepower",hp_str);
//    params.put("year",year_str);
//    params.put("touque",tourque_str);
//    params.put("mph",speed_str);
//    params.put("user_id", PrefMangr.getInstance().getUserId());
//
//    params.put("cover_photo_id", PrefMangr.getInstance().getCoverPicID());\
//            params.put("user_id", PrefMangr.getInstance().getUserId());
//
//            params.put("cover_photo_id",PrefMangr.getInstance().getCoverPicID());
//            params.put("award_name",name);
//            params.put("award_date",award_date);

            entity.addPart("award_name", new StringBody(award_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("award_date", new StringBody(award_date, "text/plain", Charset.forName("UTF-8")));

            //	entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            //	entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_AwardEditN0Image(String action, String award_id, String award_name, String award_date
            , byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            //  entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            Log.d("action 111", "" + action);
            Log.d("user_id111", "" + user_id);
            Log.d("555551111", "" + picture);
            //Log.d("pic_type11111",""+pic_type);
            entity.addPart("award_id", new StringBody(award_id, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("award_name", new StringBody(award_name, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("award_date", new StringBody(award_date, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
					/*JSONObject jobj1=jObj.getJSONObject("company_details");
					setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_Add_ride(String action, String user_id,
                                      byte[] image_aaray, String Description) {

        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 150000);  // allow 5 seconds to create the server connection
            HttpConnectionParams.setSoTimeout(httpParameters, 150000);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (image_aaray.length > 0) {


                entity.addPart("image_aaray", new ByteArrayBody(image_aaray, "image/jpeg", "test1.jpg"));
                Log.d("55555", "" + image_aaray);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("description ", new StringBody(Description, "text/plain", Charset.forName("UTF-8")));
            int statusCode = -1;

            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    Log.d("jObj//////", " " + jObj);
//					setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
                    /*JSONObject jobj1=jObj.getJSONObject("company_details");
                    setResult(jObj.getString("msg"));
					setCompany_details(jObj.getJSONObject("company_details"));
					Log.d("msg", " " + jObj.getString("msg"));
					Log.d("company_details", " " + jObj.getJSONObject("company_details"));*/
                    Log.d("msg", " " + jObj.getString("msg"));
//					Log.d("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    Log.d("msgexp", " " + e);
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                Log.d("msgexp1", " " + e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("msgexp2", " " + e);
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }
}






