package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chigo on 2017/3/21.
 * Email:isllyend@gmail.com
 */

public class Alarms {
    @SerializedName("level")
    public String level;
    @SerializedName("title")
    public String title;
    @SerializedName("txt")
    public String txt;
}
