package mx.bigapps.unionrides.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mx.bigapps.unionrides.Adapter.Comment_Adapter;
import mx.bigapps.unionrides.Application.PrefMangr;
import mx.bigapps.unionrides.Fragment.Posts;
import mx.bigapps.unionrides.Fragment.PostsWall;
import mx.bigapps.unionrides.Model.Comments;
import mx.bigapps.unionrides.R;
import mx.bigapps.unionrides.utils.Apis;

/**
 * Created by dev on 01-11-2017.
 */

public class Add_Comment extends Activity {
    ArrayList<String> personNames = new ArrayList(Arrays.asList("Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting....", "Hey, I liked your post, keep posting...."));
    ImageView comments_back;
    ImageView comment_send;
    EditText chat_edtext;
    String postId, position;
    Comments comments;
    ArrayList<Comments> commentsArrayList;
    RecyclerView recyclerView;
    TextView nocomments;
    PostsWall postsWall=new PostsWall();
    Posts posts=new Posts();

    @Override
    protected void onCreate(Bundle savedIntancestate) {
        super.onCreate(savedIntancestate);
        setContentView(R.layout.add_comment);

        nocomments = (TextView) findViewById(R.id.nocomments);
        comments_back = (ImageView) findViewById(R.id.comments_back);
        comment_send = (ImageView) findViewById(R.id.comment_send);
        chat_edtext = (EditText) findViewById(R.id.chat_edtext);

        recyclerView = (RecyclerView) findViewById(R.id.messagesContainer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        position = intent.getStringExtra("position");
        showpostcommentlist(postId);
        comments_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               posts.repost(Add_Comment.this, Integer.parseInt(position));

                postsWall.repost(Add_Comment.this, Integer.parseInt(position));
                finish();
            }
        });
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComments();
               /* personNames.add(chat_edtext.getText().toString());
                // comment_adapter.notifyDataSetChanged();
                chat_edtext.setText("");
                chat_edtext.setInputType(InputType.TYPE_NULL);
                recyclerView.scrollToPosition(personNames.size() - 1);*/
            }
        });

    }

    private void postComments() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        Log.d("Postcomment", response.toString());
                        chat_edtext.setText("");
                        showpostcommentlist(postId);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Postcomment");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postId);
                params.put("comment", chat_edtext.getText().toString());
                Log.d("Postcomment", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void showpostcommentlist(final String postId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String msg;
                        JSONObject obj = null;
                        String imagethumb;
                        commentsArrayList = new ArrayList<>();
                        Log.d("showpostcommentlist", response.toString());
                        try {
                            obj = new JSONObject(response.toString());
                            JSONArray jsonArray = obj.getJSONArray("users");
                            if (jsonArray.length() == 0) {
                                nocomments.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jarray = jsonArray.getJSONObject(i);
                                    String user_id = jarray.getString("user_id");
                                    String fullname = jarray.getString("fullname");
                                    String profileimage = jarray.getString("profileimage");
                                    String comment_id = jarray.getString("comment_id");
                                    String datetime = jarray.getString("datetime");
                                    String comment = jarray.getString("comments");
                                    JSONArray profile_image_thumb_size = jarray.getJSONArray("profile_image_thumb_size");
                                    if (profile_image_thumb_size.length() != 0) {
                                        JSONObject jarrayvideo = profile_image_thumb_size.getJSONObject(0);
                                        imagethumb = jarrayvideo.getString("80_80");
                                    } else {
                                        imagethumb = null;
                                    }
                                    comments = new Comments(fullname, profileimage, comment_id, comment, datetime);
                                    commentsArrayList.add(comments);
                                    Comment_Adapter comment_adapter = new Comment_Adapter(Add_Comment.this, commentsArrayList);
                                    recyclerView.setAdapter(comment_adapter);
                                    nocomments.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                              /*  userPost_adapter = new UserPost_Adapter(getActivity(), userPostsArrayList);
                                userPost.setAdapter(userPost_adapter);
                                userPost.smoothScrollToPosition(userPostsArrayList.size() - 1);
*/

                                }
                            }
                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }


                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "showpostcommentlist");
                params.put("user_id", PrefMangr.getInstance().getUserId());
                params.put("post_id", postId);
                Log.d("showpostcommentlist", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        postsWall.repost(Add_Comment.this, Integer.parseInt(position));
        posts.repost(Add_Comment.this, Integer.parseInt(position));
        finish();

    }
}
