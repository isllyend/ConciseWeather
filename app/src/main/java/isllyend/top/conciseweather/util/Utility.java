package isllyend.top.conciseweather.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import isllyend.top.conciseweather.bean.Serach;
import isllyend.top.conciseweather.db.City;
import isllyend.top.conciseweather.db.County;
import isllyend.top.conciseweather.db.Province;
import isllyend.top.conciseweather.gson.Weather;

/**
 * Created by Chigo on 2017/1/3.
 * Email:isllyend@gmail.com
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的json数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather data service 3.0");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Serach> handleSerachData(String response, String inputStr, Context context) {
        try {
            List<Serach> list=new ArrayList<>();
            JSONArray jsonArray=new JSONArray(response);
//            Log.e("Chigo",jsonArray.length()+"size");
            Pattern p1 = Pattern.compile("^[A-Za-z]+$");
            Pattern p2 = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
            Matcher m1= p1.matcher(inputStr);
            Matcher m2= p2.matcher(inputStr);
            Log.e("Chigo","m1="+m1.matches()+"  m2="+m2.matches());
            if (m1.matches()||m2.matches()){
                if (m1.matches()){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                       /* if (inputStr.equals(jsonObject.getString("cityEn"))){
                            Serach serach=new Serach();
                            serach.setLoc1(jsonObject.getString("cityZh"));
                            serach.setLoc2(jsonObject.getString("provinceZh")+" "+"leaderZh");
                            list.add(serach);
                        }*/
                        if (inputStr.equals(jsonObject.getString("leaderEn"))){
                            Serach serach=new Serach();
                            serach.setLoc1(jsonObject.getString("cityZh"));
                            serach.setLoc2(jsonObject.getString("provinceZh")+" "+jsonObject.getString("leaderZh"));
                            list.add(serach);
                        }
                    }
                }
                if (m2.matches()){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if (inputStr.equals(jsonObject.getString("leaderZh"))){
                                Serach serach=new Serach();
                                serach.setLoc1(jsonObject.getString("cityZh"));
                                serach.setLoc2(jsonObject.getString("provinceZh")+"  "+jsonObject.getString("leaderZh"));
                                list.add(serach);

                        }
                    }
                }

            }else {
//                Toast.makeText(context, "请输入纯英文或者纯中文@_@", Toast.LENGTH_SHORT).show();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
