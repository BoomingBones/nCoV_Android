package com.boomingbones.ncov_mvvm.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boomingbones.ncov_mvvm.bean.News;
import com.boomingbones.ncov_mvvm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<List<News>> newsData;

    public NewsViewModel() {
        newsData = new MutableLiveData<>();
        getDataFromWeb();
    }

    public LiveData<List<News>> getNewsData() {
        return newsData;
    }

    private void getDataFromWeb() {
        String address = "https://ncov.dxy.cn/ncovh5/view/pneumonia";

        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // TODO
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                Matcher matcher = Pattern.compile("(?<=Service1 = ).*?(?=\\}catch)").matcher(responseString);
                String jsonString = null;
                if (matcher.find()) {
                    jsonString = matcher.group();
                }

                Gson gson = new Gson();
                Type type = new TypeToken<List<News>>(){}.getType();
                List<News> newsList = gson.fromJson(jsonString, type);

                if (newsList != null) {
                    newsData.postValue(newsList);
                }
            }
        });
    }
}