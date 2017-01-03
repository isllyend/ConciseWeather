package isllyend.top.conciseweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Chigo on 2017/1/3.
 * Email:isllyend@gmail.com
 */

public class HttpUtil {
    public static void sendOkhttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);

    }
}
