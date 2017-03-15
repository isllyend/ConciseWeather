package isllyend.top.conciseweather.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;

import isllyend.top.conciseweather.R;

/**
 * Created by Chigo on 2017/3/2.
 * Email:isllyend@gmail.com
 */

public class SettingFragment extends BaseFragment {
    private Button btn_back;
    private TextView tv_title;
    private Button btn_add;
    private RelativeLayout relativeLayout;
    private ImageView iv_imag1,iv_imag2,iv_imag3,iv_right1,iv_right2;
    private TextView tv_title1,tv_title2,tv_title3;
    private SwitchButton sb1,sb2,sb3,sb4,sb5,sb6;
    @Override
    protected void findViews(View view) {
        btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.relaout_titlebar);
        iv_imag1= (ImageView) view.findViewById(R.id.iv_img1);
        iv_imag2= (ImageView) view.findViewById(R.id.iv_img2);
        iv_imag3= (ImageView) view.findViewById(R.id.iv_img3);
        iv_right1= (ImageView) view.findViewById(R.id.iv_right1);
        iv_right2= (ImageView) view.findViewById(R.id.iv_right2);
        tv_title1= (TextView) view.findViewById(R.id.tv_title_1);
        tv_title2= (TextView) view.findViewById(R.id.tv_title_2);
        tv_title3= (TextView) view.findViewById(R.id.tv_title_3);
        sb1= (SwitchButton) view.findViewById(R.id.switch_button1);
        sb2= (SwitchButton) view.findViewById(R.id.switch_button2);
        sb3= (SwitchButton) view.findViewById(R.id.switch_button3);
        sb4= (SwitchButton) view.findViewById(R.id.switch_button4);
        sb5= (SwitchButton) view.findViewById(R.id.switch_button5);
        sb6= (SwitchButton) view.findViewById(R.id.switch_button6);
    }

    @Override
    protected void initEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected void initView() {
        btn_add.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        tv_title.setText(bundle.getString("title_name"));

        int currentColorId= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getInt("defaultColorId",-1);
        if (currentColorId!=-1){
            relativeLayout.setBackgroundColor(getResources().getColor(currentColorId));
            iv_imag1.setColorFilter(getResources().getColor(currentColorId));
            iv_imag2.setColorFilter(getResources().getColor(currentColorId));
            iv_imag3.setColorFilter(getResources().getColor(currentColorId));
            tv_title1.setTextColor(getResources().getColor(currentColorId));
            tv_title2.setTextColor(getResources().getColor(currentColorId));
            tv_title3.setTextColor(getResources().getColor(currentColorId));
            iv_right1.setColorFilter(getResources().getColor(currentColorId));
            iv_right2.setColorFilter(getResources().getColor(currentColorId));

        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.fragment_setting;
    }
}
