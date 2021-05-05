package com.boomingbones.ncov_mvvm.ui.overview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boomingbones.ncov_mvvm.bean.Area;
import com.boomingbones.ncov_mvvm.bean.Domestic;
import com.boomingbones.ncov_mvvm.bean.Global;
import com.boomingbones.ncov_mvvm.bean.Overview;
import com.boomingbones.ncov_mvvm.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OverviewViewModel extends ViewModel {

    private final MutableLiveData<Overview> overviewData;

    public OverviewViewModel() {
        overviewData = new MutableLiveData<>();
        getDataFromWeb();
    }

    public LiveData<Overview> getOverviewData() {
        return overviewData;
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
                Gson gson = new Gson();
                final List<Area> provincesData;
                final List<Area> countriesData;
                String jsonString = null;
                Matcher matcher;

                matcher = Pattern.compile(
                        "(?<=Service = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonElement element = jsonObject.get("globalStatistics");
                final Domestic domestic = gson.fromJson(jsonString, Domestic.class);
                final Global global = gson.fromJson(element, Global.class);

                Type type = new TypeToken<List<Area>>(){}.getType();
                matcher = Pattern.compile(
                        "(?<=2true = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                countriesData = gson.fromJson(jsonString, type);

                matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                provincesData = gson.fromJson(jsonString, type);

                if (domestic != null && provincesData != null &&
                        global != null && countriesData != null) {
                    overviewData.postValue(
                            new Overview(domestic, provincesData, global, countriesData));
                }
            }
        });
    }
}