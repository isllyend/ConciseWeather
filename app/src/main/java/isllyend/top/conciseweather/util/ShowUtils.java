package isllyend.top.conciseweather.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.adapter.PopupWindowAdapter;
import isllyend.top.conciseweather.adapter.PopupWindowAdapter2;
import isllyend.top.conciseweather.gson.Weather;

/**
 * Created by Chigo on 2017/1/11.
 * Email:isllyend@gmail.com
 */

public class ShowUtils {

    public static PopupWindow createPw(int layoutId, Context context, Weather weather){
        View view= LayoutInflater.from(context).inflate(layoutId,null);
        //获取屏幕高度
        int height=ScreenUtils.getDispaly(context).heightPixels;
        PopupWindow popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, height/10*8,false);
        popupWindow.setFocusable(true);
        PopupWindowAdapter popupWindowAdapter=new PopupWindowAdapter(weather,context);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv_forecast_more);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(popupWindowAdapter);
        return popupWindow;
    }

    public static PopupWindow createPw2(int layoutId, Context context, Weather weather){

        int height=ScreenUtils.getDispaly(context).heightPixels;
        View view= LayoutInflater.from(context).inflate(layoutId,null);
        PopupWindow popupWindow=new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, height/10*8,false);
        popupWindow.setFocusable(true);
        PopupWindowAdapter2 popupWindowAdapter=new PopupWindowAdapter2(weather,context);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv_hourly_forecast_more);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(popupWindowAdapter);
        return popupWindow;
    }
}
