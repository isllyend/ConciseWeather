package isllyend.top.conciseweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.john.waveview.WaveView;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import isllyend.top.conciseweather.adapter.PopupWindowAdapter;
import isllyend.top.conciseweather.gson.DailyForecast;
import isllyend.top.conciseweather.gson.HourlyForecast;
import isllyend.top.conciseweather.gson.Weather;
import isllyend.top.conciseweather.service.AutoUpdateService;
import isllyend.top.conciseweather.util.HttpUtil;
import isllyend.top.conciseweather.util.ShowUtils;
import isllyend.top.conciseweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout,hourlyforecast;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView fluText;
    private TextView uvText;
    private TextView travelText;
    private ImageView bingPicImg;
    private TextView flText,presText;
    private TextView tv_forecast_more,tv_hourlyforecast_more;
    public DrawerLayout drawerLayout;
    public Button navButton;
    public PullToRefreshView swipeRefreshLayout;
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //背景图和状态栏融合
        if (Build.VERSION.SDK_INT>=21){
            View v=getWindow().getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        hourlyforecast= (LinearLayout) findViewById(R.id.hourlyforecast_layout);
        apiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        fluText= (TextView) findViewById(R.id.flu_text);
        uvText= (TextView) findViewById(R.id.uv_text);
        travelText= (TextView) findViewById(R.id.travel_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        flText= (TextView) findViewById(R.id.fl_text);
        presText= (TextView) findViewById(R.id.pres_text);
        tv_forecast_more= (TextView) findViewById(R.id.tv_forecast_more);
        tv_hourlyforecast_more= (TextView) findViewById(R.id.tv_hourlyforecast_more);
        swipeRefreshLayout= (PullToRefreshView) findViewById(R.id.swipe_refresh);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//禁止滑动弹出
        navButton= (Button) findViewById(R.id.nav_button);
        waveView= (WaveView) findViewById(R.id.wave_view);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String bingPic = prefs.getString("bing_pic", null);
        final String weatherId;
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        swipeRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        tv_forecast_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherString = prefs.getString("weather", null);
                Weather weather = Utility.handleWeatherResponse(weatherString);
                PopupWindow popupWindow= ShowUtils.createPw(R.layout.forecast_more,WeatherActivity.this,weather);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(view,0,20);
                backgroundAlpha(0.3f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });
        tv_hourlyforecast_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherString = prefs.getString("weather", null);
                Weather weather = Utility.handleWeatherResponse(weatherString);
                PopupWindowAdapter popupWindowAdapter=new PopupWindowAdapter(weather,WeatherActivity.this);
                PopupWindow popupWindow= ShowUtils.createPw2(R.layout.hourly_forecast_more,WeatherActivity.this,weather);

//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(view,0,20);
                backgroundAlpha(0.3f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 处理展示weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = "更新于"+weather.basic.update.updateTime;
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        int count=0;
        for (DailyForecast forecast : weather.dailyForecastlist) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            final ImageView condIcon= (ImageView) view.findViewById(R.id.cond_icon);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            final String condCode="http://files.heweather.com/cond_icon/"+forecast.more.code_d+".png";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(condCode).into(condIcon);
                }
            });
            forecastLayout.addView(view);
            if (count==0){
                dateText.setText("今天");
            }
            if (count==3){
                break;
            }
            count++;
        }
        //24小时天气预报
        hourlyforecast.removeAllViews();
        for (int i = 0; i < 5; i++) {
            HourlyForecast forecast=weather.hourlyForecastList.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.hourly_forecast_item, null);
            TextView time = (TextView) view.findViewById(R.id.tv_time);
            TextView tv_tmp = (TextView) view.findViewById(R.id.tv_tmp_item);
            final ImageView iv = (ImageView) view.findViewById(R.id.iv_img);
            time.setText(forecast.date.split(" ")[1]);
            tv_tmp.setText(forecast.tmp + "℃");
            final String condCode = "http://files.heweather.com/cond_icon/" + weather.dailyForecastlist.get(0).more.code_d + ".png";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(condCode).into(iv);
                }
            });
            hourlyforecast.addView(view);
        }
        if (weather.aqi != null) {
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            flText.setText(weather.now.fl);
            presText.setText(weather.now.pres);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        String flu="感冒指数："+weather.suggestion.flu.info;
        final String trav="旅游建议："+weather.suggestion.travel.info;
        String uv="紫外线指数："+weather.suggestion.uv.info;

        fluText.setText(flu);
        travelText.setText(trav);
        uvText.setText(uv);
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intentService=new Intent(this, AutoUpdateService.class);
        startService(intentService);
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        final Weather weatherRe;
        String weatherUrl = "http://apis.baidu.com/heweather/pro/weather?cityid="+weatherId;
        HttpUtil.sendOkhttpRequest2(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }


    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
