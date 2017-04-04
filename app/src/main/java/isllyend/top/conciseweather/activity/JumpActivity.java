package isllyend.top.conciseweather.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import isllyend.top.conciseweather.R;

public class JumpActivity extends BaseActivity {
    private FragmentManager manager;
    private FragmentTransaction transtation;
    private Toolbar toolbar;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    @Override
    protected void findView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void initEvent() {
        manager = getSupportFragmentManager();
        transtation = manager.beginTransaction();

        Intent intent = getIntent();
        String str = intent.getStringExtra("nav_item_name");
        Bundle bundle = new Bundle();
        switch (str) {
            case "城市管理":
                CityCtrlFragment cityCtrlFragment = new CityCtrlFragment();
                transtation.replace(R.id.frame_layout, cityCtrlFragment, "cityCtrlFragment");
                bundle.putString("title_name", str);
                cityCtrlFragment.setArguments(bundle);
                break;
            case "应用设置":
                SettingFragment settingFragment=new SettingFragment();
                transtation.replace(R.id.frame_layout,settingFragment,"settingFragment");
                bundle.putString("title_name", str);
                settingFragment.setArguments(bundle);
                break;
            case "关于":
                AboutFragment aboutFragment = new AboutFragment();
                transtation.replace(R.id.frame_layout, aboutFragment, "aboutFragment");
                bundle.putString("title_name", str);
                aboutFragment.setArguments(bundle);
                break;
        }
        transtation.commit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int setViewId() {
        return R.layout.activity_jump;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        spf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=spf.edit();
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    String imagePath=null;
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                       imagePath= handleImageOnKitKat(data);

                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        imagePath=handleImageBeforeKitKat(data);
                 }
                    editor.putString("bg_weather",imagePath);
                    editor.commit();
                    Toast.makeText(this, "设置成功！重启生效！", Toast.LENGTH_SHORT).show();
                }else if (resultCode==RESULT_CANCELED){
                    Toast.makeText(this, "取消了~", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:

                if (resultCode==RESULT_OK){
                    String imagePath=null;
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        imagePath= handleImageOnKitKat(data);

                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        imagePath=handleImageBeforeKitKat(data);
                    }
                    editor.putString("bg_welcome",imagePath);
                    editor.commit();
                    Toast.makeText(this, "设置成功！下次生效！", Toast.LENGTH_SHORT).show();
                }else if (resultCode==RESULT_CANCELED){
                    Toast.makeText(this, "取消了~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor =getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @TargetApi(19)
    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;

    }
}
