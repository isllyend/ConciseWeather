package isllyend.top.conciseweather.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.bean.Serach;

/**
 * Created by Chigo on 09/14/2016.
 */
public class SerachResultAdapter extends BaseAdapter {
    private List<Serach> list;
    private Context context;
//    private OnSetImageLocation onSetImageLocation;

    public SerachResultAdapter(List<Serach> list, Context context) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_serach_result,parent,false);
            convertView.setTag(viewHolder);

            viewHolder.tv_loc= (TextView) convertView.findViewById(R.id.tv_loc);
            viewHolder.tv_loc2= (TextView) convertView.findViewById(R.id.tv_loc2);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //填充数据
        Serach city=list.get(position);
        int currentColorId= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getInt("defaultColorId",-1);

        viewHolder.tv_loc.setText(city.getLoc1());
        if (currentColorId!=-1){
            viewHolder.tv_loc.setTextColor(context.getResources().getColor(currentColorId));
        }
        viewHolder.tv_loc2.setText(city.getLoc2());
        return convertView;
    }

    public static   class ViewHolder{
        TextView tv_loc;
        TextView tv_loc2;
    }


}
