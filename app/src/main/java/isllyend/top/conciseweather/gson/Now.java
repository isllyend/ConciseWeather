package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chigo on 2017/1/5.
 * Email:isllyend@gmail.com
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;

        @SerializedName("code")
        public String code;
    }

    @SerializedName("fl")
    public String fl;
    @SerializedName("pres")
    public String pres;
}
