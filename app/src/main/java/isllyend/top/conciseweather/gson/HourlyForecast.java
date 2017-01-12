package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chigo on 2017/1/12.
 * Email:isllyend@gmail.com
 */

public class HourlyForecast {
    @SerializedName("date")
    public String date;
    @SerializedName("tmp")
    public  String tmp;
    @SerializedName("wind")
    public Wind wind;
    public class Wind{
        public String dir;
        public String sc;
        public String spd;
    }
}
