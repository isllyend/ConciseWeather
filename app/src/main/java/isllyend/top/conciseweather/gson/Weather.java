package isllyend.top.conciseweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chigo on 2017/1/5.
 * Email:isllyend@gmail.com
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastlist;
}
