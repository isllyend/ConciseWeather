package isllyend.top.conciseweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.bean.Suggestion;

/**
 * Created by Chigo on 09/14/2016.
 */
public class SuggestionAdapter extends BaseAdapter {
    private List<Suggestion> list;
    private Context context;

    public SuggestionAdapter(List<Suggestion> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (list.size()+1)/2;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_suggestion_item,parent,false);
            convertView.setTag(viewHolder);

            viewHolder.tv_bry= (TextView) convertView.findViewById(R.id.tv_brf);
            viewHolder.tv_type= (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);

            viewHolder.tv_bry2= (TextView) convertView.findViewById(R.id.tv_brf2);
            viewHolder.tv_type2= (TextView) convertView.findViewById(R.id.tv_type2);
            viewHolder.iv_icon2= (ImageView) convertView.findViewById(R.id.iv_icon2);


        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        //填充数据
        //需要判断集合里的元素
        //当为偶数个的时候填充每一行的一个数据

        final Suggestion suggestion=list.get(position*2);
        viewHolder.tv_bry.setText(suggestion.getBrf());
        viewHolder.tv_type.setText(suggestion.getType());
        Glide.with(context).load(suggestion.getImgId()).into(viewHolder.iv_icon);


        //判断如果集合为奇数个就把最后一行个隐藏
        if ((position*2+1)==list.size()){
            //
        }else {
            Suggestion suggestion2=list.get(position*2+1);
            viewHolder.tv_bry2.setText(suggestion2.getBrf());
            viewHolder.tv_type2.setText(suggestion2.getType());
            Glide.with(context).load(suggestion2.getImgId()).into(viewHolder.iv_icon2);
        }
        return convertView;
    }

    public  class ViewHolder{
        TextView tv_bry,tv_type;
        TextView tv_bry2,tv_type2;
        ImageView iv_icon;
        ImageView iv_icon2;
    }
}
