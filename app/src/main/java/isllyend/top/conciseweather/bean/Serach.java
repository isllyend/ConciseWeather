package isllyend.top.conciseweather.bean;

/**
 * Created by Chigo on 2017/3/2.
 * Email:isllyend@gmail.com
 */

public class Serach {
    private String loc1;
    private String loc2;
    public Serach(){

    }
    public Serach(String loc1, String loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public String getLoc1() {
        return loc1;
    }

    public void setLoc1(String loc1) {
        this.loc1 = loc1;
    }

    public String getLoc2() {
        return loc2;
    }

    public void setLoc2(String loc2) {
        this.loc2 = loc2;
    }
}
