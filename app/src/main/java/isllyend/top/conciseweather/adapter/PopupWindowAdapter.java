package isllyend.top.conciseweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.gson.DailyForecast;
import isllyend.top.conciseweather.gson.Weather;
import isllyend.top.conciseweather.util.GlideUtil;

/**
 * Created by Chigo on 2017/1/10.
 * Email:isllyend@gmail.com
 */
public class PopupWindowAdapter extends RecyclerView.Adapter<PopupWindowAdapter.ViewHolder> {
    private Weather weather;
    private Context context;
    public PopupWindowAdapter(Weather weather,Context context) {
        this.weather = weather;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_more_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<DailyForecast> data=weather.dailyForecastlist;
        DailyForecast dailyForecast=data.get(position);
        holder.tv_date.setText(dailyForecast.date);
        holder.tv_degree.setText(dailyForecast.temperature.max+"℃/"+dailyForecast.temperature.min+"℃");
        holder.tv_cond.setText("白天："+dailyForecast.more.info+"\n夜间："+dailyForecast.more.info2);
        holder.tv_wind.setText("风向："+dailyForecast.wind.dir+dailyForecast.wind.sc+"级");
        holder.tv_astro.setText("日出/日落："+dailyForecast.astro.sr+"/"+dailyForecast.astro.ss);
        final String condCode="http://files.heweather.com/cond_icon/"+dailyForecast.more.code_d+".png";
//                Glide.with(context).load(condCode).into(holder.iv_left_img);
        GlideUtil.loadIntoUseFitWidth(context,condCode,R.mipmap.ic_error,holder.iv_left_img);
    }

    @Override
    public int getItemCount() {
        return weather.dailyForecastlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date,tv_degree,tv_wind,tv_cond,tv_astro;
        ImageView iv_left_img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_date= (TextView) itemView.findViewById(R.id.tv_date);
            tv_degree= (TextView) itemView.findViewById(R.id.tv_degree);
            tv_astro= (TextView) itemView.findViewById(R.id.tv_astro);
            tv_cond= (TextView) itemView.findViewById(R.id.tv_cond);
            tv_wind= (TextView) itemView.findViewById(R.id.tv_wind);
            iv_left_img= (ImageView) itemView.findViewById(R.id.iv_left_img);
        }
    }
}
