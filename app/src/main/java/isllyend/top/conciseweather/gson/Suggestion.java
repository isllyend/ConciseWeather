package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chigo on 2017/1/5.
 * Email:isllyend@gmail.com
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    @SerializedName("trav")
    public Travel travel;

    @SerializedName("air")
    public Air air;

    public UV uv;
    @SerializedName("drsg")
    public Dress dress;

    public Flu flu;

    public class Comfort {
        @SerializedName("brf")
        public String info;
    }

    public class CarWash {
        @SerializedName("brf")
        public String info;
    }

    public class Sport {
        @SerializedName("brf")
        public String info;
    }

    public class Dress {
        @SerializedName("brf")
        public String info;
    }

    public class Flu {
        @SerializedName("brf")
        public String info;
    }

    public class Travel {
        @SerializedName("brf")
        public String info;
    }

    public class UV {
        @SerializedName("brf")
        public String info;
    }


    public class Air {
        @SerializedName("brf")
        public String info;
    }
}
