package isllyend.top.conciseweather;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Chigo on 2017/2/7.
 * Email:isllyend@gmail.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //android 5.0 状态栏可控制
        if (Build.VERSION.SDK_INT>20){

            View decorView=getWindow().getDecorView();
            //活动的布局会显示在状态栏上
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(setViewId());
        findView();
        initView();
        initEvent();
        loadData();
    }

    //初始化控件
    protected abstract void findView();

    // 初始化点击事件
    protected abstract void initEvent();

    //初始化界面
    protected abstract void initView();

    //加载数据
    protected abstract void loadData();

    //返回布局id
    protected abstract int setViewId();


}