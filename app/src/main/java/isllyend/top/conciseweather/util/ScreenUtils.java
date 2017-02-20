package isllyend.top.conciseweather.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Chigo on 2017/1/12.
 * Email:isllyend@gmail.com
 */

/**
 * 获取屏幕高宽工具类
 */
public class ScreenUtils {
    public static DisplayMetrics getDispaly(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics ;
    }
}
