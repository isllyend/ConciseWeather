package isllyend.top.conciseweather.activity;

import android.view.View;
import android.view.WindowManager;

import isllyend.top.conciseweather.R;

/**
 * Created by Chigo on 2017/3/1.
 * Email:isllyend@gmail.com
 */

public class MyFragment extends BaseFragment {
    @Override
    protected void findViews(View view) {
        //取消状态栏
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        return R.layout.myfragment_item;
    }
}
