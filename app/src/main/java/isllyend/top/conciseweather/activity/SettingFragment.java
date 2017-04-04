package isllyend.top.conciseweather.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.util.AlbumUtils;

/**
 * Created by Chigo on 2017/3/2.
 * Email:isllyend@gmail.com
 */

public class SettingFragment extends BaseFragment {
    private Button btn_back;
    private TextView tv_title;
    private Button btn_add;
    private RelativeLayout relativeLayout;
    private ImageView iv_imag1,iv_imag2,iv_imag3,iv_right1,iv_right2;
    private TextView tv_title1,tv_title2,tv_title3;
    private SwitchButton sb2,sb3,sb4;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void findViews(View view) {
        btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        relativeLayout= (RelativeLayout) view.findViewById(R.id.relaout_titlebar);
        iv_imag1= (ImageView) view.findViewById(R.id.iv_img1);
        iv_imag2= (ImageView) view.findViewById(R.id.iv_img2);
        iv_imag3= (ImageView) view.findViewById(R.id.iv_img3);
        iv_right1= (ImageView) view.findViewById(R.id.iv_right1);
        iv_right2= (ImageView) view.findViewById(R.id.iv_right2);
        tv_title1= (TextView) view.findViewById(R.id.tv_title_1);
        tv_title2= (TextView) view.findViewById(R.id.tv_title_2);
        tv_title3= (TextView) view.findViewById(R.id.tv_title_3);
        sb2= (SwitchButton) view.findViewById(R.id.switch_button2);
        sb3= (SwitchButton) view.findViewById(R.id.switch_button3);
        sb4= (SwitchButton) view.findViewById(R.id.switch_button4);
    }

    @Override
    protected void initEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        sb2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    editor.putInt("bl_sb2",1);
                }else {
                    editor.putInt("bl_sb2",0);
                }
                editor.commit();
            }
        });
        sb3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    editor.putInt("bl_sb3",1);
                }else {
                    editor.putInt("bl_sb3",0);
                }
                editor.commit();
            }
        });
        sb4.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    editor.putInt("bl_sb4",1);
                }else {
                    editor.putInt("bl_sb4",0);
                }
                editor.commit();
            }
        });

        iv_right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext(),"自定义主页壁纸",1);
            }
        });
        iv_right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getContext(),"自定义欢迎页面壁纸",2);
            }
        });

    }




    @Override
    protected void initView() {
        btn_add.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        tv_title.setText(bundle.getString("title_name"));
        preferences=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor=preferences.edit();
        int currentColorId= preferences.getInt("defaultColorId",-1);
        if (currentColorId!=-1){
            relativeLayout.setBackgroundColor(getResources().getColor(currentColorId));
            iv_imag1.setColorFilter(getResources().getColor(currentColorId));
            iv_imag2.setColorFilter(getResources().getColor(currentColorId));
            iv_imag3.setColorFilter(getResources().getColor(currentColorId));
            tv_title1.setTextColor(getResources().getColor(currentColorId));
            tv_title2.setTextColor(getResources().getColor(currentColorId));
            tv_title3.setTextColor(getResources().getColor(currentColorId));
            iv_right1.setColorFilter(getResources().getColor(currentColorId));
            iv_right2.setColorFilter(getResources().getColor(currentColorId));
        }
        int isOpen2=preferences.getInt("bl_sb2",-1);
        int isOpen3=preferences.getInt("bl_sb3",-1);
        int isOpen4=preferences.getInt("bl_sb4",-1);

        if (isOpen2!=1){
            sb2.setChecked(false);
        }else {
            sb2.setChecked(true);
        }
        if (isOpen3!=1){
            sb3.setChecked(false);
        }else {
            sb3.setChecked(true);
        }
        if (isOpen4!=1){
            sb4.setChecked(false);
        }else {
            sb4.setChecked(true);
        }

    }
    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.fragment_setting;
    }

   public void showDialog(final Context context, String title, final int num){
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
       builder.setIcon(R.mipmap.ic_background);
        builder.setTitle(title);
       builder.setPositiveButton("选取图片", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               AlbumUtils.openAlbum(getActivity(),num);
           }
       });
       builder.setNegativeButton("恢复默认", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
                if (num==1){
                    editor.putString("bg_weather",null);
                }else {
                    editor.putString("bg_welcome",null);
                }
               editor.commit();
               Toast.makeText(getActivity(), "重启生效哦！", Toast.LENGTH_SHORT).show();
           }
       });

       builder.show();

   }
}
