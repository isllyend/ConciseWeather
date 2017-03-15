package isllyend.top.conciseweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wayww.edittextfirework.FireworkView;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import isllyend.top.conciseweather.R;
import isllyend.top.conciseweather.adapter.SerachResultAdapter;
import isllyend.top.conciseweather.bean.Serach;
import isllyend.top.conciseweather.util.HttpUtil;
import isllyend.top.conciseweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class CityChooseActivity extends BaseActivity {
    private FrameLayout frameLayout;
    private FireworkView fireworkView;
    private EditText et_search;
    private LinearLayout linearlayout;
    private TextView tv_tile;
    private Button btn_back,btn_add;
    private String serUrl;
    private String serResult;
    private ListView lv_serach_result;
    private List<Serach> serachs;
    private SerachResultAdapter adapter;
    private SharedPreferences prefs;
    private final int RESULT_CODE=2;
    private TextView tv_one,tv_one_title;
    private RelativeLayout relative_titlebar;

    @Override
    protected void findView() {
        linearlayout= (LinearLayout) findViewById(R.id.linearlayout);
        frameLayout= (FrameLayout) findViewById(R.id.choose_city_frame_layout);
        fireworkView= (FireworkView) findViewById(R.id.fire_work);
        et_search= (EditText) findViewById(R.id.et_serach);
        tv_tile= (TextView) findViewById(R.id.tv_title);
        btn_add= (Button) findViewById(R.id.btn_add);
        btn_back= (Button) findViewById(R.id.btn_back);
        lv_serach_result= (ListView) findViewById(R.id.lv_result);
        tv_one= (TextView) findViewById(R.id.tv_one);
        tv_one_title= (TextView) findViewById(R.id.tv_one_title);
        relative_titlebar= (RelativeLayout) findViewById(R.id.relaout_titlebar);
    }

    @Override
    protected void initEvent() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_serach_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("CityName",serachs.get(i).getLoc1());
                setResult(RESULT_CODE,intent);
                finish();
            }
        });

    }

    @Override
    protected void initView() {
        int defaultId=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("defaultColorId",-1);
        if (defaultId!=-1){
            tv_one.setBackgroundColor(getResources().getColor(defaultId));
            tv_one.setTextColor(getResources().getColor(defaultId));
            et_search.setTextColor(getResources().getColor(defaultId));
            relative_titlebar.setBackgroundColor(getResources().getColor(defaultId));
        }
        serachs=new ArrayList<>();
        adapter=new SerachResultAdapter(serachs,this);
        lv_serach_result.setAdapter(adapter);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        serResult = prefs.getString("serach", null);
        serUrl="http://files.heweather.com/china-city-list.json";
        tv_tile.setText("搜索城市");
        btn_add.setVisibility(View.GONE);
        fireworkView.bindEditText(et_search);
        Intent intent=getIntent();
        int code=getIntent().getIntExtra("code",-1);
        switch (code){
            case 1:
                linearlayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
               FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                AreaFragment areaFragment = new AreaFragment();
                transaction.add(R.id.choose_city_frame_layout, areaFragment, "areafragment");
                transaction.commit();
                break;
            case 2:
                LitePal.useDefault();
                et_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            Log.e("Chigo","before==="+charSequence+"i="+i+"i1="+i1+"i2="+i2);
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        Log.e("Chigo","ontext==="+charSequence+"i="+i+"i1="+i1+"i2="+i2);
                        //打开
                        /*InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(et_search.getWindowToken(), 0);*/

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        serachs.clear();
                        Log.e("Chigo","afger==="+editable.toString());
                        if (TextUtils.isEmpty(serResult)){
                            qureySerachData();
                        }
                        serResult = prefs.getString("serach", null);
                        List<Serach> temp= Utility.handleSerachData(serResult,editable.toString(),getApplicationContext());
                       if (temp!=null){
                           serachs.addAll(temp);
                          /* //关闭软键盘
                           InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                           imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);*/
                       }else {
                           Toast.makeText(CityChooseActivity.this, "很皮！没有该城市@_@", Toast.LENGTH_SHORT).show();
                       }
                        adapter.notifyDataSetChanged();



                    }
                });
                break;
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.activity_city_choose;
    }
    public void qureySerachData(){
            HttpUtil.sendOkhttpRequest(serUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(CityChooseActivity.this, "网络又调皮了@_@", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        serResult=response.body().string();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(CityChooseActivity.this).edit();
                        editor.putString("serach", serResult);
                        editor.apply();
                    }
                }
            });
        }
}
