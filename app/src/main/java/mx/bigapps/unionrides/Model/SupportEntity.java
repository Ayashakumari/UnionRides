package mx.bigapps.unionrides.Model;

/**
 * Created by lenovo on 11/10/2017.
 */
public class SupportEntity {

    String otheruser_id,user_id,user_name,typing_status,chat_from ;

    public String getOtheruser_id() {
        return otheruser_id;
    }

    public void setOtheruser_id(String otheruser_id) {
        this.otheruser_id = otheruser_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTyping_status() {
        return typing_status;
    }

    public void setTyping_status(String typing_status) {
        this.typing_status = typing_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getChat_from() {
        return chat_from;
    }

    public void setChat_from(String chat_from) {
        this.chat_from = chat_from;
    }
}
