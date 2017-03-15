package isllyend.top.conciseweather.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import isllyend.top.conciseweather.R;



/**
 * Created by Chigo on 2017/3/1.
 * Email:isllyend@gmail.com
 */

public class AboutFragment extends BaseFragment {
    private Button btn_back;
    private Button btn_add;
    private TextView tv_title;
    private RelativeLayout relativeLayout;
    private ImageView iv_imag1,iv_imag2;
    private TextView tv_title1,tv_title2;
    private TextView tv_version;
    @Override
    protected void findViews(View view) {
        btn_back= (Button) view.findViewById(R.id.btn_back);
        btn_add= (Button) view.findViewById(R.id.btn_add);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.relaout_titlebar);
        iv_imag1= (ImageView) view.findViewById(R.id.iv_imag1);
        iv_imag2= (ImageView) view.findViewById(R.id.iv_imag2);

        tv_title1= (TextView) view.findViewById(R.id.tv_title1);
        tv_title2= (TextView) view.findViewById(R.id.tv_title2);
        tv_version= (TextView) view.findViewById(R.id.tv_app_version);
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
        Bundle bundle=getArguments();
        tv_title.setText( bundle.getString("title_name"));
        int currentColorId= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getInt("defaultColorId",-1);
        if (currentColorId!=-1){
            relativeLayout.setBackgroundColor(getResources().getColor(currentColorId));
            iv_imag1.setColorFilter(getResources().getColor(currentColorId));
            iv_imag2.setColorFilter(getResources().getColor(currentColorId));
            tv_title1.setTextColor(getResources().getColor(currentColorId));
            tv_title2.setTextColor(getResources().getColor(currentColorId));
            tv_version.setTextColor(getResources().getColor(currentColorId));
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.fragment_about;
    }
}
