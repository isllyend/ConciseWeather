package isllyend.top.conciseweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import isllyend.top.conciseweather.R;

public class JumpActivity extends BaseActivity {
    private FragmentManager manager;
    private FragmentTransaction transtation;
    private Toolbar toolbar;
    @Override
    protected void findView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initEvent() {
        manager = getSupportFragmentManager();
        transtation = manager.beginTransaction();

        Intent intent = getIntent();
        String str = intent.getStringExtra("nav_item_name");
        Bundle bundle = new Bundle();
        switch (str) {
            case "城市管理":
                CityCtrlFragment cityCtrlFragment = new CityCtrlFragment();
                transtation.replace(R.id.frame_layout, cityCtrlFragment, "cityCtrlFragment");
                bundle.putString("title_name", str);
                cityCtrlFragment.setArguments(bundle);
                break;
            case "应用设置":
                SettingFragment settingFragment=new SettingFragment();
                transtation.replace(R.id.frame_layout,settingFragment,"settingFragment");
                bundle.putString("title_name", str);
                settingFragment.setArguments(bundle);
                break;
            case "关于":
                AboutFragment aboutFragment = new AboutFragment();
                transtation.replace(R.id.frame_layout, aboutFragment, "aboutFragment");
                bundle.putString("title_name", str);
                aboutFragment.setArguments(bundle);
                break;
        }
        transtation.commit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.activity_jump;
    }
}
