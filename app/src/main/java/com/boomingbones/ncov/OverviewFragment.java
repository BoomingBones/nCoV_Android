package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.boomingbones.ncov.struct.Area;
import com.boomingbones.ncov.struct.Domestic;
import com.boomingbones.ncov.struct.Global;
import com.boomingbones.ncov.tools.HttpUtil;
import com.boomingbones.ncov.tools.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private static final int FRAGMENT_UPDATE_FINISH = 10002;

    private View view;
    private Context context;
    private Handler handler;

    public OverviewFragment(Handler handler) {
        // Required empty public constructor
        this.handler = handler;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_overview, container, false);
        context = getContext();

        initFragment();

        return view;
    }

    private void initFragment() {

        String address = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show();

                        view.findViewById(R.id.tabLayout).setVisibility(View.VISIBLE);
                        Message message = new Message();
                        message.what = FRAGMENT_UPDATE_FINISH;
                        handler.sendMessage(message);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                String jsonString = null;
                Gson gson = new Gson();
                final ArrayList<Area> provinceList;
                final ArrayList<Area> countryList;
                Matcher matcher;
                JsonObject object;

                matcher = Pattern.compile("(?<=Service = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                object = new JsonParser().parse(jsonString).getAsJsonObject();
                JsonElement element = object.get("globalStatistics");
                final Domestic domestic = gson.fromJson(jsonString, Domestic.class);
                final Global global = gson.fromJson(element, Global.class);

                matcher = Pattern.compile("(?<=2true = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                countryList = gson.fromJson(jsonString, new TypeToken<List<Area>>(){}.getType());

                matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                provinceList = gson.fromJson(jsonString, new TypeToken<List<Area>>(){}.getType());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment[] fragments = {
                                new DomesticFragment(domestic, provinceList, context),
                                new GlobalFragment(global, countryList, context)};
                        ViewPager viewPager = view.findViewById(R.id.viewPager);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(
                                getChildFragmentManager(), fragments);
                        viewPager.setAdapter(adapter);

                        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
                        tabLayout.setupWithViewPager(viewPager);

                        view.findViewById(R.id.tabLayout).setVisibility(View.VISIBLE);
                    }
                });

                Message message = new Message();
                message.what = FRAGMENT_UPDATE_FINISH;
                handler.sendMessage(message);
            }
        });
    }
}
