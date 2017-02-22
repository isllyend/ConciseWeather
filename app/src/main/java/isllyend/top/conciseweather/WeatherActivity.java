package isllyend.top.conciseweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import isllyend.top.conciseweather.adapter.HourlyForecastAdapter;
import isllyend.top.conciseweather.adapter.SuggestionAdapter;
import isllyend.top.conciseweather.bean.Suggestion;
import isllyend.top.conciseweather.custom.RippleImageView;
import isllyend.top.conciseweather.gson.DailyForecast;
import isllyend.top.conciseweather.gson.Weather;
import isllyend.top.conciseweather.service.AutoUpdateService;
import isllyend.top.conciseweather.util.ChartUtil;
import isllyend.top.conciseweather.util.DensityUtils;
import isllyend.top.conciseweather.util.HttpUtil;
import isllyend.top.conciseweather.util.ScreenUtils;
import isllyend.top.conciseweather.util.ShowUtils;
import isllyend.top.conciseweather.util.Utility;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

import static isllyend.top.conciseweather.util.ScreenUtils.getDispaly;


public class WeatherActivity extends BaseActivity {
    private NestedScrollView weatherLayout;
    private TextView titleCity;/**/
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout, ll_aqi;
    private RecyclerView hourlyforecast;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView fluText;
    private TextView uvText;
    private TextView travelText;
    private ImageView bingPicImg;
    private TextView flText, presText;
    private TextView tv_forecast_more;
    private Toolbar toolbar;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tv_fl;
    private HourlyForecastAdapter hourlyForecastAdapter;

    public PullToRefreshView swipeRefreshLayout;

    private LineChartView lineChart;
    private Weather weather;
    private WeatherView mWeatherView;
    private RippleImageView rippleImageView;

    String cityName, weatherString, bingPic, weatherName;

    private List<String> maxTemp, minTemp;
    private isllyend.top.conciseweather.custom.WeatherView sunriseView;
    private ListView lv_suggestion;
    private SuggestionAdapter suggestionAdapter;
    private List<isllyend.top.conciseweather.bean.Suggestion> suggestions;

//    private String currentTime;
    @Override
    protected void findView() {
        //初始化控件
        weatherLayout = (NestedScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        hourlyforecast = (RecyclerView) findViewById(R.id.hourlyforecast_layout);
        ll_aqi = (LinearLayout) findViewById(R.id.ll_aqi);
        apiText = (TextView) findViewById(R.id.aqi_text);
       /* pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        fluText = (TextView) findViewById(R.id.flu_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        travelText = (TextView) findViewById(R.id.travel_text);*/
//        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        flText = (TextView) findViewById(R.id.fl_text);
        presText = (TextView) findViewById(R.id.pres_text);
        tv_forecast_more = (TextView) findViewById(R.id.tv_forecast_more);
        swipeRefreshLayout = (PullToRefreshView) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//禁止滑动弹出
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_fl = (TextView) findViewById(R.id.tv_fl);
        lineChart = (LineChartView) findViewById(R.id.line_chart);
        mWeatherView = (WeatherView) findViewById(R.id.weatherview);
        sunriseView= (isllyend.top.conciseweather.custom.WeatherView) findViewById(R.id.sunrise);
        rippleImageView= (RippleImageView) findViewById(R.id.rippleImageView);
        lv_suggestion= (ListView) findViewById(R.id.lv_suggestion);

    }


    @Override
    protected void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(null, cityName);
            }
        });


        tv_forecast_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String weatherString = prefs.getString("weather", null);
                weather = Utility.handleWeatherResponse(weatherString);
                if (weather == null) {
                    loadData();
                }
                PopupWindow popupWindow = ShowUtils.createPw(R.layout.forecast_more, WeatherActivity.this, weather);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(view, 0, 20);
                backgroundAlpha(0.3f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                mWeatherView.startAnimation();
                return true;
            }
        });
    }

    @Override
    protected void initView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        weatherString = prefs.getString("weather", null);
        bingPic = prefs.getString("bing_pic", null);
        cityName = getIntent().getStringExtra("cityName");
        mWeatherView.startAnimation();
        maxTemp = new ArrayList<>();
        minTemp = new ArrayList<>();
      /*  Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23
        int minute = t.minute;
        currentTime=hour+":"+minute;*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyforecast.setLayoutManager(linearLayoutManager);


        //加载toolbar
        setSupportActionBar(toolbar);

        //设置toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        //初始化listview
        suggestions=new ArrayList<>();
        suggestionAdapter=new SuggestionAdapter(suggestions,this);
        lv_suggestion.setAdapter(suggestionAdapter);


    }

    @Override
    protected void loadData() {

      /*  if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }*/

        if (weatherString != null) {
            //有缓存时直接解析天气数据
            weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(null, cityName);
        }


    }

    @Override
    protected int setViewId() {
        return R.layout.activity_weather;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
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
        String updateTime = weather.basic.update.updateTime.split(" ")[1] + " 刷新";
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
       if (weatherInfo.contains("雨")){
           mWeatherView.setVisibility(View.VISIBLE);
           mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                   .setLifeTime(3000)
                   .setFadeOutTime(1000)
                   .setParticles(43)
                   .setFPS(60)
                   .setAngle(-5)
                   .setOrientationMode(Constants.orientationStatus.ENABLE)
                   .startAnimation();
       }else if (weatherInfo.contains("雪")) {
           mWeatherView.setVisibility(View.VISIBLE);
           mWeatherView.setWeather(Constants.weatherStatus.SNOW)
                   .setLifeTime(3000)
                   .setFadeOutTime(1000)
                   .setParticles(43)
                   .setFPS(60)
                   .setAngle(-5)
                   .setOrientationMode(Constants.orientationStatus.ENABLE);
       }else if(weatherInfo.contains("晴")){
           rippleImageView.setVisibility(View.VISIBLE);
           rippleImageView.startWaveAnimation();
       }
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        tv_fl.setText("体感 " + weather.now.fl);
        hourlyForecastAdapter = new HourlyForecastAdapter(weather);
        hourlyforecast.setAdapter(hourlyForecastAdapter);
        int count = 0;
        for (DailyForecast forecast : weather.dailyForecastlist) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            final ImageView condIcon = (ImageView) view.findViewById(R.id.cond_icon);
            String dateTextStr = forecast.date.substring(forecast.date.indexOf("-") + 1);
            dateText.setText(dateTextStr);
            infoText.setText(forecast.more.info);

            maxTemp.add(forecast.temperature.max);
            minTemp.add(forecast.temperature.min);
            final String condCode = "http://files.heweather.com/cond_icon/" + forecast.more.code_d + ".png";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(condCode).into(condIcon);
                }
            });
            int width = ScreenUtils.getDispaly(this).widthPixels - DensityUtils.dp2px(this, 20);
            float laytWidht = forecastLayout.getWidth();
            float realWidth = width / 7;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = (int) realWidth;
            view.setLayoutParams(params);
            forecastLayout.addView(view);
            if (count == 0) {
                dateText.setText("今天");
            }
            if (count == 6) {
                break;
            }
            count++;
        }
        loadLineChart(maxTemp, minTemp, lineChart);

        //24小时天气预报
      /*  hourlyforecast.removeAllViews();
        for (int i = 0; i < 5; i++) {
            HourlyForecast forecast = weather.hourlyForecastList.get(i);
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
        }*/
        if (weather.aqi != null) {
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            flText.setText(weather.now.fl);
            presText.setText(weather.now.pres);
            int airLevel = Integer.parseInt(weather.now.fl);
            String airLeverStr = "";
        } else {
            ll_aqi.setVisibility(View.GONE);
        }
        //suggestion
        String comfort = weather.suggestion.comfort.info;
        String carWash = weather.suggestion.carWash.info;
        String sport =weather.suggestion.sport.info;
        String flu = weather.suggestion.flu.info;
        String trav =weather.suggestion.travel.info;
        String uv = weather.suggestion.uv.info;
        String dsrg=weather.suggestion.dress.info;
        String air=weather.suggestion.air.info;
        List<Suggestion> temp=new ArrayList<>();
        Suggestion sug_dsrg=new Suggestion(dsrg,R.mipmap.sug_clouth,"穿衣");
        temp.add(sug_dsrg);
        Suggestion sug_uv=new Suggestion(uv,R.mipmap.sug_uv,"紫外线");
        temp.add(sug_uv);
        Suggestion sug_flu=new Suggestion(flu,R.mipmap.sug_flu,"感冒");
        temp.add(sug_flu);
        Suggestion sug_comf=new Suggestion(comfort,R.mipmap.sug_air,"气候");
        temp.add(sug_comf);
        Suggestion sug_run=new Suggestion(sport,R.mipmap.sug_run,"运动");
        temp.add(sug_run);
        Suggestion sug_washCar=new Suggestion(carWash,R.mipmap.sug_washcar,"洗车");
        temp.add(sug_washCar);
        Suggestion sug_tral=new Suggestion(trav,R.mipmap.sug_tral,"出行");
        temp.add(sug_tral);
        Suggestion sug_air=new Suggestion(dsrg,R.mipmap.sug_air,"空气");
        temp.add(sug_air);

        suggestions.addAll(temp);
        suggestionAdapter.notifyDataSetChanged();

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intentService = new Intent(this, AutoUpdateService.class);
        startService(intentService);

        sunriseView.setDefaultTime(weather.dailyForecastlist.get(0).astro.sr,weather.dailyForecastlist.get(0).astro.ss);
        sunriseView.setmCurrentTime(updateTime);

    }



    /**
     * 根据天气id,城市请求城市天气信息。
     */
    public void requestWeather(final String weatherId, final String cityName) {
        final Weather weatherRe;
        String weatherUrl = null;
        if (cityName == null) {
            weatherUrl = "http://apis.baidu.com/heweather/pro/weather?cityid=" + weatherId;
        } else {
            weatherUrl = "http://apis.baidu.com/heweather/pro/weather?city=" + cityName;
        }

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
//        loadBingPic();
    }


    /**
     * 加载必应背景图
     */
    private void loadBingPic() {
        int widthSc = getDispaly(getApplicationContext()).widthPixels;
        int heightSc = getDispaly(getApplicationContext()).heightPixels;
        String requestBingPic = "http://guolin.tech/api/bing_pic";
//        String requestBingPic = "http://lorempixel.com/"+widthSc+"/"+heightSc+"/city/";
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
    /*    Bitmap bitmap=HttpUtil.getHttpBitmap(requestBingPic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes=baos.toByteArray();
        Glide.with(WeatherActivity.this)
                .load(bytes)
                .into(bingPicImg);*/
    }

    /**
     * 设置toolbar左侧按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
               if ( mWeatherView.isPlaying()){
                   mWeatherView.stopAnimation();
               }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    public void loadLineChart(List<String> temp, List<String> temp2, LineChartView lineChart) {

        ChartUtil.getAxisPoints(temp, temp2);
        ChartUtil.initLineChart(lineChart);
        if (minTemp != null && !minTemp.isEmpty()) {
            minTemp.clear();
        }
        if (maxTemp != null && !maxTemp.isEmpty()) {
           maxTemp.clear();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        rippleImageView.stopWaveAnimation();
        mWeatherView.stopAnimation();
    }
}
