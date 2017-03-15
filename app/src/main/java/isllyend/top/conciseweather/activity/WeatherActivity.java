package isllyend.top.conciseweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.adapter.HourlyForecastAdapter;
import isllyend.top.conciseweather.adapter.SuggestionAdapter;
import isllyend.top.conciseweather.bean.Suggestion;
import isllyend.top.conciseweather.custom.RippleImageView;
import isllyend.top.conciseweather.db.CityCtrl;
import isllyend.top.conciseweather.gson.DailyForecast;
import isllyend.top.conciseweather.gson.Weather;
import isllyend.top.conciseweather.service.AutoUpdateService;
import isllyend.top.conciseweather.util.ActivityCollector;
import isllyend.top.conciseweather.util.ChartUtil;
import isllyend.top.conciseweather.util.DensityUtils;
import isllyend.top.conciseweather.util.GlideUtil;
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

import static isllyend.top.conciseweather.util.Utility.handleWeatherResponse;


public class WeatherActivity extends BaseActivity {
    private NestedScrollView weatherLayout;
    private TextView titleCity;/**/
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private RecyclerView hourlyforecast;
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

    private String weatherString, weatherName;

    private List<String> maxTemp, minTemp;
    private List<CityCtrl> cityList;
    private isllyend.top.conciseweather.custom.WeatherView sunriseView;
    private ListView lv_suggestion;
    private SuggestionAdapter suggestionAdapter;
    private List<isllyend.top.conciseweather.bean.Suggestion> suggestions;
    private long exitTime = 0;

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
        tv_forecast_more = (TextView) findViewById(R.id.tv_forecast_more);
        swipeRefreshLayout = (PullToRefreshView) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//禁止滑动弹出
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_fl = (TextView) findViewById(R.id.tv_fl);
        lineChart = (LineChartView) findViewById(R.id.line_chart);
        mWeatherView = (WeatherView) findViewById(R.id.weatherview);
        sunriseView = (isllyend.top.conciseweather.custom.WeatherView) findViewById(R.id.sunrise);
        rippleImageView = (RippleImageView) findViewById(R.id.rippleImageView);
        lv_suggestion = (ListView) findViewById(R.id.lv_suggestion);
    }


    @Override
    protected void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String currentCityName = (String) titleCity.getText();
                requestWeather(null, currentCityName);
            }
        });


        tv_forecast_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String weatherString = prefs.getString("weather", null);
                weather = handleWeatherResponse(weatherString);
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
                Intent intent = new Intent(WeatherActivity.this, JumpActivity.class);
                switch (item.getItemId()) {
                    case R.id.nav_crl_city:
                        intent.putExtra("nav_item_name", "城市管理");
                        startActivity(intent);
                        break;
                    case R.id.nav_theme:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View layout = inflater.inflate(R.layout.dialog_theme, null);//获取自定义布局
                        builder.setView(layout);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);

                        final int currentId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("defRadioId", -1);
                        if (currentId != -1) {
                            radioGroup.check(currentId);
                        } else {
                            radioGroup.check(R.id.radio_2);
                        }

                        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup rg, int i) {

                                switch (i) {
                                    case R.id.radio_1:
                                        editor.putInt("defRadioId", R.id.radio_1);
                                        editor.putInt("defaultColorId", R.color.theme_purple);
                                        break;
                                    case R.id.radio_2:
                                        editor.putInt("defRadioId", R.id.radio_2);
                                        editor.putInt("defaultColorId", R.color.colorPrimary);
                                        break;
                                    case R.id.radio_3:
                                        editor.putInt("defRadioId", R.id.radio_3);
                                        editor.putInt("defaultColorId", R.color.theme_orange);
                                        break;
                                    case R.id.radio_4:
                                        editor.putInt("defRadioId", R.id.radio_4);
                                        editor.putInt("defaultColorId", R.color.theme_green);
                                        break;
                                    case R.id.radio_5:
                                        editor.putInt("defRadioId", R.id.radio_5);
                                        editor.putInt("defaultColorId", R.color.theme_gray);
                                        break;
                                }
                                dialog.dismiss();
                                editor.apply();
                            }
                        });
                        break;
                    case R.id.nav_chk_version:
                        drawerLayout.closeDrawers();
                        Toast.makeText(WeatherActivity.this, "当前为最新版本", Toast.LENGTH_SHORT).show();
                       /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("")
                                .setMessage("当前为最新版本")
                                .setPositiveButton("",null)
                                .show();*/
                        break;
                    case R.id.nav_setting:
                        intent.putExtra("nav_item_name", "应用设置");
                        startActivity(intent);
                        break;
                    case R.id.nav_about_me:
                        intent.putExtra("nav_item_name", "关于");
                        startActivity(intent);
                        break;
                }

                item.setCheckable(false);
                drawerLayout.closeDrawers();
                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final int currentColorId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("defaultColorId", -1);
                Log.e("Chigo", "currentColorId===" + currentColorId);
                if (currentColorId != -1) {
                    changeColor(currentColorId);
                } else {
                    changeColor(R.color.colorPrimary);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                stopAllWeatherAnim();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mWeatherView.startAnimation();
                rippleImageView.startWaveAnimation();

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void changeColor(int colorId) {

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{getResources().getColor(colorId),
                getResources().getColor(colorId)
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        navigationView.setItemIconTintList(colorStateList);
        navigationView.setItemTextColor(colorStateList);
    }

    @Override
    protected void initView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        weatherString = prefs.getString("weather", null);
        weatherName = getIntent().getStringExtra("weather_name");
        mWeatherView.startAnimation();
        maxTemp = new ArrayList<>();
        minTemp = new ArrayList<>();
        cityList = new ArrayList<>();

        //设置进入的显示位置
        weatherInfoText.setFocusable(true);
        weatherInfoText.setFocusableInTouchMode(true);
        weatherInfoText.requestFocus();

        LitePalDB litePalDB = new LitePalDB("CityCtrl", 1);
        litePalDB.addClassName(CityCtrl.class.getName());
        LitePal.use(litePalDB);
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
        suggestions = new ArrayList<>();
        suggestionAdapter = new SuggestionAdapter(suggestions, this);
        lv_suggestion.setAdapter(suggestionAdapter);

    }

    @Override
    protected void loadData() {
        CityCtrl ctrl = null;
        List<CityCtrl> list = DataSupport.findAll(CityCtrl.class);
        for (CityCtrl c : list) {
            if (c.getDefNum() == 1) {
                ctrl = c;
            }
        }
        Weather weather = Utility.handleWeatherResponse(weatherString);
        Intent intent = getIntent();
        String ctrlName = intent.getStringExtra("city_crtl_name");
        String mainCityName = intent.getStringExtra("cityName");

        if (weather != null && weather.basic.cityName.equals(mainCityName) && !TextUtils.isEmpty(weatherString)) {
            showWeatherInfo(weather);
        } else {
            if (!TextUtils.isEmpty(ctrlName)) {
                requestWeather(null, ctrlName);
            }
            if (!TextUtils.isEmpty(mainCityName)) {
                requestWeather(null, mainCityName);
            }
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
        stopAllWeatherAnim();
        sunriseView.setStarTime(weather.dailyForecastlist.get(0).astro.sr);
        sunriseView.setEndTime(weather.dailyForecastlist.get(0).astro.ss);
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1] + " 刷新";
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        if (weatherInfo.contains("阴")) {
            drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDark));
        } else {
            drawerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        //开启天气动画
        startWeatherAnim(weatherInfo);
        titleCity.setText(cityName);

        //find all data
//        DataSupport.deleteAll(CityCtrl.class);
        List<CityCtrl> ctrls = DataSupport.where("location=?", cityName).find(CityCtrl.class);
        CityCtrl current = new CityCtrl(cityName, weatherInfo, degree);
        if (ctrls.size() == 0 || ctrls.isEmpty()) {
            current.setDefNum(1);
            current.save();
        } else {
            for (CityCtrl c : ctrls) {
//                DataSupport.deleteAll(CityCtrl.class,"location=?",current.getLocation());
                if (c.getLocation().equals(current.getLocation())) {
                    c.setWeatherInfo(current.getWeatherInfo());
                    c.setWeatherTmp(current.getWeatherTmp());
                    c.save();
                }
            }

        }
        if (weatherName != null) {
            titleCity.setText(weatherName);
        }
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
            ImageView condIcon = (ImageView) view.findViewById(R.id.cond_icon);
            String dateTextStr = forecast.date.substring(forecast.date.indexOf("-") + 1);
            dateText.setText(dateTextStr);
            infoText.setText(forecast.more.info);

            maxTemp.add(forecast.temperature.max);
            minTemp.add(forecast.temperature.min);
            String condStr = "http://files.heweather.com/cond_icon/" + forecast.more.code_d + ".png";
            GlideUtil.loadIntoUseFitWidth(this, condStr, R.mipmap.ic_error, condIcon);
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


        //suggestion
        String comfort = weather.suggestion.comfort.info;
        String carWash = weather.suggestion.carWash.info;
        String sport = weather.suggestion.sport.info;
        String flu = weather.suggestion.flu.info;
        String trav = weather.suggestion.travel.info;
        String uv = weather.suggestion.uv.info;
        String dsrg = weather.suggestion.dress.info;
        String air = weather.suggestion.air.info;
        List<Suggestion> temp = new ArrayList<>();
        Suggestion sug_dsrg = new Suggestion(dsrg, R.mipmap.sug_clouth, "穿衣");
        temp.add(sug_dsrg);
        Suggestion sug_uv = new Suggestion(uv, R.mipmap.sug_uv, "紫外线");
        temp.add(sug_uv);
        Suggestion sug_flu = new Suggestion(flu, R.mipmap.sug_flu, "感冒");
        temp.add(sug_flu);
        Suggestion sug_comf = new Suggestion(comfort, R.mipmap.sug_air, "气候");
        temp.add(sug_comf);
        Suggestion sug_run = new Suggestion(sport, R.mipmap.sug_run, "运动");
        temp.add(sug_run);
        Suggestion sug_washCar = new Suggestion(carWash, R.mipmap.sug_washcar, "洗车");
        temp.add(sug_washCar);
        Suggestion sug_tral = new Suggestion(trav, R.mipmap.sug_tral, "出行");
        temp.add(sug_tral);
        Suggestion sug_air = new Suggestion(dsrg, R.mipmap.sug_air, "空气");
        temp.add(sug_air);

        suggestions.addAll(temp);
        suggestionAdapter.notifyDataSetChanged();

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intentService = new Intent(this, AutoUpdateService.class);
        startService(intentService);


    }


    /**
     * 根据天气id,城市请求城市天气信息。
     */
    public void requestWeather(final String weatherId, final String cityName) {
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
                final Weather weather = handleWeatherResponse(responseText);
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
                stopAllWeatherAnim();
                break;

            case R.id.setting:
                Intent intent = new Intent(WeatherActivity.this, JumpActivity.class);
                intent.putExtra("nav_item_name", "应用设置");
                startActivity(intent);
                break;

            case R.id.about:
                Intent intent1 = new Intent(WeatherActivity.this, JumpActivity.class);
                intent1.putExtra("nav_item_name", "关于");
                startActivity(intent1);
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

    /**
     * 按两次返回键才退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出简约天气", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 开启天气动画
     *
     * @param weatherString
     */
    public void startWeatherAnim(String weatherString) {
        if (weatherString.contains("雨")) {
            mWeatherView.setVisibility(View.VISIBLE);
            mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                    .setLifeTime(3000)
                    .setFadeOutTime(1000)
                    .setParticles(43)
                    .setFPS(60)
                    .setAngle(-5)
                    .setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();
        } else if (weatherString.contains("雪")) {
            mWeatherView.setVisibility(View.VISIBLE);
            mWeatherView.setWeather(Constants.weatherStatus.SNOW)
                    .setLifeTime(3000)
                    .setFadeOutTime(1000)
                    .setParticles(43)
                    .setFPS(60)
                    .setAngle(-5)
                    .setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();
        } else if (weatherString.contains("晴")) {
            rippleImageView.setVisibility(View.VISIBLE);
            rippleImageView.startWaveAnimation();
        }

    }

    /**
     * 关闭所有天气动画
     */
    public void stopAllWeatherAnim() {
        if (mWeatherView.isPlaying()) {
            mWeatherView.stopAnimation();
        }
        if (rippleImageView.isPlaying()) {
            rippleImageView.stopWaveAnimation();
        }
    }
}
