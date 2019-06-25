
package mx.bigapps.unionrides.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Demo {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Demo() {
    }

    /**
     * 
     * @param response
     * @param msg
     */
    public Demo(String msg, List<Response> response) {
        super();
        this.msg = msg;
        this.response = response;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
