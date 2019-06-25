package mx.bigapps.unionrides.Model;

/**
 * Created by admin on 11-12-2017.
 */
public class awarddata {
    String awardname, awarddate, awardimage;

    public awarddata(String awardname, String awarddate, String awardimage) {
        this.awardname = awardname;
        this.awarddate = awarddate;
        this.awardimage = awardimage;
    }

    public String getAwardname() {
        return awardname;
    }

    public void setAwardname(String awardname) {
        this.awardname = awardname;
    }

    public String getAwarddate() {
        return awarddate;
    }

    public void setAwarddate(String awarddate) {
        this.awarddate = awarddate;
    }

    public String getAwardimage() {
        return awardimage;
    }

    public void setAwardimage(String awardimage) {
        this.awardimage = awardimage;
    }
}

