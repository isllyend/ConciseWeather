package isllyend.top.conciseweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.gson.HourlyForecast;
import isllyend.top.conciseweather.gson.Weather;

/**
 * Created by Chigo on 2017/1/10.
 * Email:isllyend@gmail.com
 */
public class PopupWindowAdapter2 extends RecyclerView.Adapter<PopupWindowAdapter2.ViewHolder> {
    private Weather weather;
    private Context context;
    public PopupWindowAdapter2(Weather weather, Context context) {
        this.weather = weather;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_more_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<HourlyForecast> data=weather.hourlyForecastList;
        HourlyForecast hourlyForecast=data.get(position);
        holder.tv_date.setText(hourlyForecast.date.split(" ")[1]);
        holder.tv_tmp.setText(hourlyForecast.tmp+"℃");
        holder.tv_wind.setText("风向："+hourlyForecast.wind.dir+hourlyForecast.wind.sc+"\n"+hourlyForecast.wind.spd+" kmph");
        final String condCode="http://files.heweather.com/cond_icon/"+weather.dailyForecastlist.get(0).more.code_d+".png";
                Glide.with(context).load(condCode).into(holder.iv_left_img);
    }

    @Override
    public int getItemCount() {
        return weather.dailyForecastlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_tmp,tv_wind;
        ImageView iv_left_img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_date= (TextView) itemView.findViewById(R.id.tv_time);
            tv_tmp= (TextView) itemView.findViewById(R.id.tv_tmp);
            iv_left_img= (ImageView) itemView.findViewById(R.id.iv_img);
            tv_wind= (TextView) itemView.findViewById(R.id.tv_wind);
        }
    }
}
