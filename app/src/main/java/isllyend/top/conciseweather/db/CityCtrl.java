package isllyend.top.conciseweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Chigo on 2017/2/26.
 * Email:isllyend@gmail.com
 */

public class CityCtrl extends DataSupport {
    private int id;

    public int getId() {
        return id;
    }


    public int defNum;



    public int getDefNum() {
        return defNum;
    }

    public void setDefNum(int defNum) {
        this.defNum = defNum;
    }


    public void setId(int id) {
        this.id = id;
    }

    private String location;
    private String weatherInfo;
    private String weatherTmp;

    public CityCtrl(String location, String weatherInfo, String weatherTmp) {
        this.location = location;
        this.weatherInfo = weatherInfo;
        this.weatherTmp = weatherTmp;
    }

    public CityCtrl(String location, String weatherInfo, String weatherTmp,int defNum) {
        this.location = location;
        this.weatherInfo = weatherInfo;
        this.weatherTmp = weatherTmp;
        this.defNum=defNum;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getWeatherTmp() {
        return weatherTmp;
    }

    public void setWeatherTmp(String weatherTmp) {
        this.weatherTmp = weatherTmp;
    }
}
