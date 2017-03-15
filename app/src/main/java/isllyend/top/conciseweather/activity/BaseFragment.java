package isllyend.top.conciseweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chigo on 2017/2/7.
 * Email:isllyend@gmail.com
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //取消状态栏
        View view = inflater.inflate(setViewId(), container, false);
        findViews(view);// 初始化控件
        initView();// 初始化界面
        initEvent();// 初始化点击事件
        loadData();// 请求数据
        return view;
    }

    protected abstract void findViews(View view);

    protected abstract void initEvent();

    protected abstract void initView();

    protected abstract void loadData();

    /**
     * 设置布局
     *
     * @return
     */
    protected abstract int setViewId();
}
