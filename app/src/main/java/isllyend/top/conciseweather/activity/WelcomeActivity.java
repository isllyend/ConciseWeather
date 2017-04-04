package isllyend.top.conciseweather.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.util.GlideUtil;
import isllyend.top.conciseweather.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static isllyend.top.conciseweather.util.ScreenUtils.getDispaly;

public class WelcomeActivity extends BaseActivity {
    private ImageView iv_wel;
    private Handler handler = new Handler();
    private SharedPreferences preferences;

    @Override
    protected void findView() {
        iv_wel = (ImageView) findViewById(R.id.iv_wel);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        boolean isFrist = preferences.getBoolean("isFrist", true);
        if (isFrist) {
            editor.putInt("bl_sb2", 1);
            editor.putInt("bl_sb3", 1);
            editor.putInt("bl_sb4", 1);
            editor.putBoolean("isFrist", false);
            editor.commit();

        }


    }

    @Override
    protected void loadData() {
        List<String> perList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            perList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
     /*   if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            perList.add(Manifest.permission.READ_PHONE_STATE);
        }*/
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            perList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!perList.isEmpty()) {
            String[] pers = perList.toArray(new String[perList.size()]);
            ActivityCompat.requestPermissions(WelcomeActivity.this, pers, 1);
        } else {
            start();
        }
    }

    @Override
    protected int setViewId() {
        return R.layout.activity_welcome;
    }


    private void start() {
        String path = preferences.getString("bg_welcome", null);
        if (path == null) {
            //加载欢迎页面
            loadBingPic();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }else {
            GlideUtil.loadImageWithPath(this, path, iv_wel);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
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
           /*     SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WelcomeActivity.this).load(bingPic).into(iv_wel);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int res : grantResults) {
                        if (res != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须开启所有权限才能使用!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    start();
                } else {
                    Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
