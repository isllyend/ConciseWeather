package isllyend.top.conciseweather.bean;

/**
 * Created by Chigo on 2017/2/22.
 * Email:isllyend@gmail.com
 */

public class Suggestion {
    private int imgId;
    private String type;
    private String brf;

    public Suggestion(String brf, int imgId, String type) {
        this.brf = brf;
        this.imgId = imgId;
        this.type = type;
    }

    public String getBrf() {
        return brf;
    }

    public void setBrf(String brf) {
        this.brf = brf;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
