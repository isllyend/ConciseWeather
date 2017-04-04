package isllyend.top.conciseweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomer.fadingtextview.FadingTextView;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.adapter.CityCtrlListViewAdapter;
import isllyend.top.conciseweather.custom.SwipeDismissListView;
import isllyend.top.conciseweather.db.CityCtrl;


/**
 * Created by Chigo on 2017/2/26.
 * Email:isllyend@gmail.com
 */

public class CityCtrlFragment extends BaseFragment implements OnMenuItemClickListener {
    private Button btn_back;
    private TextView tv_title;
    private Button btn_add;
    private SwipeDismissListView swipeDismissListView;
    private List<CityCtrl> dataLists;
    private CityCtrlListViewAdapter cityCtrlListViewAdapter;
    private final int REQUEST_CODE = 11;
    private final int RESULT_CODE = 22;
    private FadingTextView fadingTextView;

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private RelativeLayout layout;
    @Override
    protected void findViews(View view) {
        btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        swipeDismissListView = (SwipeDismissListView) view.findViewById(R.id.dis_listview);
        layout= (RelativeLayout) view.findViewById(R.id.relaout_titlebar);
        fadingTextView= (FadingTextView) view.findViewById(R.id.ftv);
    }

    @Override
    protected void initEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        swipeDismissListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                String cityName = dataLists.get(i).getLocation();
                intent.putExtra("city_crtl_name", cityName);
                startActivity(intent);
                getActivity().finish();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*//取消状态栏
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
            }
        });

        swipeDismissListView.setOnDismissCallback(new SwipeDismissListView.OnDismissCallback() {

            @Override
            public void onDismiss(int dismissPosition) {
//                cityCtrlListViewAdapter.remove(adapter.getItem(dismissPosition));

                CityCtrl ctrl = dataLists.get(dismissPosition);
                DataSupport.deleteAll(CityCtrl.class, "location=?", ctrl.getLocation());
                dataLists.remove(dismissPosition);
                cityCtrlListViewAdapter.notifyDataSetChanged();
            }
        });
        //设置item长按事件
        swipeDismissListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("").
                        setMessage("是否设置为默认城市").
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int z) {
                                for (int j = 0; j < swipeDismissListView.getChildCount(); j++) {
                                    View v = swipeDismissListView.getChildAt(j);
                                    CityCtrlListViewAdapter.ViewHolder viewHolder = (CityCtrlListViewAdapter.ViewHolder) v.getTag();
                                    viewHolder.iv_location.setVisibility(View.INVISIBLE);
                                    CityCtrl ctrl = dataLists.get(j);
                                    ctrl.setDefNum(0);
                                    ctrl.save();
                                }
                                View v = swipeDismissListView.getChildAt(i);
                                CityCtrlListViewAdapter.ViewHolder viewHolder = (CityCtrlListViewAdapter.ViewHolder) v.getTag();
                                viewHolder.iv_location.setVisibility(View.VISIBLE);
                                CityCtrl ctrl = dataLists.get(i);
                                ctrl.setDefNum(1);
                                ctrl.save();
                            }
                        }).
                        setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).
                        show();


//                ImageView iv_location= (ImageView) v.findViewById(R.id.iv_location);
//                iv_location.setVisibility(View.INVISIBLE);
                cityCtrlListViewAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initView() {
         int currentColorId= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getInt("defaultColorId",-1);
        if (currentColorId!=-1){
            layout.setBackgroundColor(getResources().getColor(currentColorId));
            fadingTextView.setTextColor(getResources().getColor(currentColorId));
        }
        LitePalDB litePalDB = new LitePalDB("CityCtrl", 1);
        LitePal.use(litePalDB);
        Bundle bundle = getArguments();
        tv_title.setText(bundle.getString("title_name"));
        dataLists = DataSupport.findAll(CityCtrl.class);
        cityCtrlListViewAdapter = new CityCtrlListViewAdapter(dataLists, getContext());
        swipeDismissListView.setAdapter(cityCtrlListViewAdapter);
        fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MyFragment myFragment = new MyFragment();
        initMenuFragment();
        transaction.add(R.id.frame_layout, myFragment, "myfragment");
        transaction.commit();
    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);

        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View clickedView, int position) {
                switch (position){
                    case 1:
                        Intent intent=new Intent(getActivity(),CityChooseActivity.class);
                        intent.putExtra("code",1);
                        startActivityForResult(intent,REQUEST_CODE);
                        break;
                    case 2:
                        Intent intent2=new Intent(getActivity(),CityChooseActivity.class);
                        intent2.putExtra("code",2);
                        startActivityForResult(intent2,REQUEST_CODE);
                        break;
                }
            }
        });
        mMenuDialogFragment.setItemLongClickListener(new OnMenuItemLongClickListener() {
            @Override
            public void onMenuItemLongClick(View clickedView, int position) {

            }
        });
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.close);

        MenuObject unauto = new MenuObject("根据省份添加");
        unauto.setResource(R.mipmap.add);

        MenuObject auto = new MenuObject("搜索添加");
        auto.setResource(R.mipmap.serach);


        menuObjects.add(close);
        menuObjects.add(unauto);
        menuObjects.add(auto);
        return menuObjects;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.fragment_city_ctrl;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LitePalDB litePalDB = new LitePalDB("CityCtrl", 1);
        LitePal.use(litePalDB);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                String cityName = data.getStringExtra("CityName");
                for (int i = 0; i < dataLists.size(); i++) {
                    if (cityName.equals(dataLists.get(i).getLocation())) {
                        Toast.makeText(getContext(), "请不要添加重复的城市！", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        if (i == dataLists.size() - 1 && !cityName.equals(dataLists.get(dataLists.size() - 1
                        ).getLocation())) {
                            CityCtrl ctrl = new CityCtrl(cityName, "N/A", "N/A°",0);
                            ctrl.save();
                            dataLists.add(ctrl);
//                            Log.e("Chigo","----------->size="+dataLists.size());
                            cityCtrlListViewAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

    }


}
