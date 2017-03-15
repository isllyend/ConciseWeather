package isllyend.top.conciseweather.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chigo on 2017/2/27.
 * Email:isllyend@gmail.com
 */

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity:activities){
            activity.finish();
        }
    }
}
