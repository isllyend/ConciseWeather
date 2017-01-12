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
        Request request=new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public static void sendOkhttpRequest2(String address,okhttp3.Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .addHeader("apikey","f0548c958a87eb740e1abb7c8de9e440")
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
