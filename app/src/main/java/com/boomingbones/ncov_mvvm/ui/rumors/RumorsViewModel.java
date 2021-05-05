package com.boomingbones.ncov_mvvm.ui.rumors;

import android.content.res.AssetManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.boomingbones.ncov_mvvm.bean.Rumor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class RumorsViewModel extends ViewModel {

    private final AssetManager assetManager;

    private final MutableLiveData<List<Rumor>> rumorsData;

    public RumorsViewModel(AssetManager assetManager) {
        this.assetManager = assetManager;
        rumorsData = new MutableLiveData<>();
        getDataFromJson();
    }

    public LiveData<List<Rumor>> getRumorsData() {
        return rumorsData;
    }

    private void getDataFromJson() {
//        StringBuilder builder = new StringBuilder();
//        BufferedReader reader = null;
//
//        try {
//            reader = new BufferedReader(
//                    new InputStreamReader(assetManager.open("rumors.json")));
//
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Rumor>>() {}.getType();

                List<Rumor> rumorList = null;
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(assetManager.open("rumors.json")));
                    rumorList = gson.fromJson(reader, type);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (rumorList != null) {
                    rumorsData.postValue(rumorList);
                }
            }
        }).start();
    }
}