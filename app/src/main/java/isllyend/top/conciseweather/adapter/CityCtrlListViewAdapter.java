package isllyend.top.conciseweather.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.db.CityCtrl;

/**
 * Created by Chigo on 09/14/2016.
 */
public class CityCtrlListViewAdapter extends BaseAdapter {
    private List<CityCtrl> list;
    private Context context;
//    private OnSetImageLocation onSetImageLocation;

    public CityCtrlListViewAdapter(List<CityCtrl> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_city_ctrl_item,parent,false);
            convertView.setTag(viewHolder);

            viewHolder.avatarImageView= (AvatarImageView) convertView.findViewById(R.id.item_avatar);
            viewHolder.tv_loc= (TextView) convertView.findViewById(R.id.tv_location);
            viewHolder.tv_weather_info= (TextView) convertView.findViewById(R.id.tv_weather_info);
            viewHolder.tv_weather_tmp= (TextView) convertView.findViewById(R.id.tv_weather_tmp);
            viewHolder.iv_location= (ImageView) convertView.findViewById(R.id.iv_location);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //填充数据
        CityCtrl city=list.get(position);
        final int currentColorId= PreferenceManager.getDefaultSharedPreferences(context).getInt("defaultColorId",-1);
        viewHolder.avatarImageView.setTextAndColor(city.getLocation().substring(0,1),android.R.color.white);
        viewHolder.tv_loc.setText(city.getLocation());
        viewHolder.tv_weather_info.setText(city.getWeatherInfo());
        if (currentColorId!=-1){
            viewHolder.avatarImageView.setBackgroundColor(context.getResources().getColor(currentColorId));
            viewHolder.tv_weather_info.setTextColor(context.getResources().getColor(currentColorId));
            viewHolder.tv_weather_tmp.setTextColor(context.getResources().getColor(currentColorId));
            viewHolder.iv_location.setColorFilter(context.getResources().getColor(currentColorId));
        }
        viewHolder.tv_weather_tmp.setText(city.getWeatherTmp());
        if (city.getDefNum()==1){
            viewHolder.iv_location.setVisibility(View.VISIBLE);
        }else {
            viewHolder.iv_location.setVisibility(View.INVISIBLE);
        }

//        onSetImageLocation.exLocation(viewHolder.iv_location);
//        onSetImageLocation.exLocation().setVisibility(View.VISIBLE);
//        onSetImageLocation.exLocation(viewHolder.iv_location);
        return convertView;
    }

    public static   class ViewHolder{
        AvatarImageView avatarImageView;
        TextView tv_loc;
        TextView tv_weather_tmp;
        TextView tv_weather_info;
        public ImageView iv_location;
    }


}
