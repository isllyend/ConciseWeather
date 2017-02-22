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
public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private Weather weather;
    private Context mContext;
    public HourlyForecastAdapter(Weather weather) {
        this.weather = weather;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.hourly_forecast_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<HourlyForecast> data=weather.hourlyForecastList;
        HourlyForecast hourlyForecast=data.get(position);
        holder.tv_time.setText(hourlyForecast.date.split(" ")[1]);
        holder.tv_tmp.setText(hourlyForecast.tmp+"℃");
        final String condCode="http://files.heweather.com/cond_icon/"+weather.dailyForecastlist.get(0).more.code_d+".png";
                Glide.with(mContext).load(condCode).into(holder.iv_img);

        if (position==0){
            holder.tv_time.setText("现在");
        }
    }

    @Override
    public int getItemCount() {
        return weather.hourlyForecastList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_tmp;
        ImageView iv_img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            tv_tmp= (TextView) itemView.findViewById(R.id.tv_tmp_item);
            iv_img= (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }
}
