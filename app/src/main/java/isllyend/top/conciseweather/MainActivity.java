package isllyend.top.conciseweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private LocationClient locationClient;
    private ProgressDialog progressDialog;
    private SharedPreferences pre;
    private String newCityName;

    @Override
    protected void findView() {

        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        pre= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        List<String> perList=new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            perList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            perList.add(Manifest.permission.READ_PHONE_STATE);
        }*/
        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            perList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }*/
        if (!perList.isEmpty()){
            String[] pers=perList.toArray(new String[perList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,pers,1);
        }else {
            requestLocation();
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

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
        option.setScanSpan(10*60*1000);//update time
        option.setIsNeedAddress(true);// show  Address
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int res:grantResults){
                        if (res!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须开启所有权限才能使用!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }
                else {
                    Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
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
        String oldCityName=pre.getString("city",null);//上次的数据
        if (oldCityName == null || oldCityName != newCityName) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("cityName", newCityName);
            startActivity(intent);
            Toast.makeText(this, "自动定位成功！当前的城市："+newCityName, Toast.LENGTH_SHORT).show();
            finish();
        } else if (oldCityName == newCityName) {
            if (pre.getString("weather", null) != null) {
                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
