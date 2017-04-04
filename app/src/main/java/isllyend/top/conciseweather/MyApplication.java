package isllyend.top.conciseweather;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Chigo on 2017/3/18.
 * Email:isllyend@gmail.com
 */

public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        ShareSDK.initSDK(this);
    }
}
