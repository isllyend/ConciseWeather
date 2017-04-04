package isllyend.top.conciseweather.util;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Chigo on 2017/3/21.
 * Email:isllyend@gmail.com
 */

public class AlbumUtils {
    public static void openAlbum(Activity activity,int num) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, num); // 打开相册
    }

}
