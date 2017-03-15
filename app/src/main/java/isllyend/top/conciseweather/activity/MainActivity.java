package isllyend.top.conciseweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.db.CityCtrl;

public class MainActivity extends BaseActivity {
    private LocationClient locationClient;
    private ProgressDialog progressDialog;
//    private SharedPreferences pre;
    private String newCityName;

    @Override
    protected void findView() {

        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
//        pre= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

}

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        LitePalDB litePalDB = new LitePalDB("CityCtrl", 1);
        litePalDB.addClassName(CityCtrl.class.getName());
        LitePal.use(litePalDB);
    }

    @Override
    protected void loadData() {
            requestLocation();
    }

    @Override
    protected int setViewId() {
        return R.layout.activity_main;
    }

    private void requestLocation(){
        showProgressDialog();
        initLocation();
        locationClient.start();

    }

    private void initLocation() {
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(60*1000);//update time
        option.setIsNeedAddress(true);// show  Address
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(option);
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String locationStr=bdLocation.getAddrStr();
            String province=locationStr.split("省")[1];
            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            if (province.split("市")[1].contains("县")){
                 newCityName=province.split("市")[1].split("县")[0];
                editor.putString("city",newCityName);
                editor.apply();
            }else {
                newCityName=province.split("市")[0];
                editor.putString("city",newCityName);
                editor.apply();
            }
            closeProgressDialog();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
//            Log.e("Chigo",s+"-->"+i);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationClient.stop();

    }
    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在定位....");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }


    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(this, WeatherActivity.class);
        List<CityCtrl> ctrls= DataSupport.findAll(CityCtrl.class);
        CityCtrl ctrl=null;
        for (CityCtrl c:ctrls){
            if (c.getDefNum()==1){
                ctrl=c;
                break;
            }
        }

        if (ctrl!=null){
            intent.putExtra("cityName", ctrl.getLocation());
            startActivity(intent);
        }else {
            intent.putExtra("cityName", newCityName);
            startActivity(intent);
            Toast.makeText(this, "自动定位成功！当前的城市："+newCityName, Toast.LENGTH_SHORT).show();
        }
            finish();
    }
}
