package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chigo on 2017/1/5.
 * Email:isllyend@gmail.com
 */

public class DailyForecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    @SerializedName("wind")
    public Wind wind;
    @SerializedName("astro")
    public Astro astro;

    public class Temperature {
        @SerializedName("max")
        public String max;
        @SerializedName("min")
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
        @SerializedName("txt_n")
        public String info2;
        @SerializedName("code_d")
        public String code_d;
    }

    public class Wind {
        @SerializedName("dir")
        public String dir;
        @SerializedName("sc")
        public String sc;
    }

    public class Astro {
        public String sr;
        public String ss;
    }
}
