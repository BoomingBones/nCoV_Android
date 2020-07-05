package com.boomingbones.ncov.tools;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
