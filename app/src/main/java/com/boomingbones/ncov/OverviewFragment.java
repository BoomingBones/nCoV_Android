package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
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

    private static final int OVERVIEW_ITEM_COUNT = 15;

    private View view;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_overview, container, false);
        context = getContext();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        initFragment(container);

        return view;
    }

    private void initFragment(final ViewGroup container) {
        swipeRefreshLayout.setRefreshing(true);

        String address = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                String jsonString = null;
                Matcher matcher;
                JsonObject object;
                Gson gson = new Gson();

                matcher = Pattern.compile("(?<=Service = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                object = new JsonParser().parse(jsonString).getAsJsonObject();
                JsonElement element = object.get("globalStatistics");
                final OverviewDomestic domestic = gson.fromJson(jsonString, OverviewDomestic.class);
                final OverviewGlobal global = gson.fromJson(element, OverviewGlobal.class);

                matcher = Pattern.compile("(?<=2true = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                final List<OverviewCountry> countryList = gson.fromJson(jsonString, new TypeToken<List<OverviewCountry>>(){}.getType());

                matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                final List<OverviewProvince> provinceList = gson.fromJson(jsonString, new TypeToken<List<OverviewProvince>>(){}.getType());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View domesticView = LayoutInflater.from(context)
                                .inflate(R.layout.fragment_overview_overall_domestic, container, false);
                        setTextViewText(domestic.currentConfirmedIncr,
                                (TextView) domesticView.findViewById(R.id.curConfirmedIncr_text),
                                R.color.curConfirmed);
                        setTextViewText(domestic.importedIncr,
                                (TextView) domesticView.findViewById(R.id.importedIncr_text),
                                R.color.imported);
                        setTextViewText(domestic.suspectIncr,
                                (TextView) domesticView.findViewById(R.id.suspectedIncr_text),
                                R.color.suspected);
                        setTextViewText(domestic.confirmedIncr,
                                (TextView) domesticView.findViewById(R.id.confirmedIncr_text),
                                R.color.confirmed);
                        setTextViewText(domestic.deadIncr,
                                (TextView) domesticView.findViewById(R.id.deathIncr_text),
                                R.color.death);
                        setTextViewText(domestic.curedIncr,
                                (TextView) domesticView.findViewById(R.id.curedIncr_text),
                                R.color.cured);
                        ((TextView) domesticView.findViewById(R.id.curConfirmedCount_text))
                                .setText(addComma(domestic.currentConfirmedCount));
                        ((TextView) domesticView.findViewById(R.id.importedCount_text))
                                .setText(addComma(domestic.importedCount));
                        ((TextView) domesticView.findViewById(R.id.suspectedCount_text))
                                .setText(addComma(domestic.suspectCount));
                        ((TextView) domesticView.findViewById(R.id.confirmedCount_text))
                                .setText(addComma(domestic.confirmedCount));
                        ((TextView) domesticView.findViewById(R.id.deathCount_text))
                                .setText(addComma(domestic.deadCount));
                        ((TextView) domesticView.findViewById(R.id.curedCount_text))
                                .setText(addComma(domestic.curedCount));
                        FrameLayout overallContainer_domestic = view.findViewById(R.id.overall_container_domestic);
                        overallContainer_domestic.addView(domesticView);

                        View foreignView = LayoutInflater.from(context)
                                .inflate(R.layout.fragment_overview_overall_foreign, container, false);
                        setTextViewText(global.currentConfirmedIncr,
                                (TextView) foreignView.findViewById(R.id.curConfirmedIncr_text),
                                R.color.curConfirmed);
                        setTextViewText(global.confirmedIncr,
                                (TextView) foreignView.findViewById(R.id.confirmedIncr_text),
                                R.color.confirmed);
                        setTextViewText(global.deadIncr,
                                (TextView) foreignView.findViewById(R.id.deathIncr_text),
                                R.color.death);
                        setTextViewText(global.curedIncr,
                                (TextView) foreignView.findViewById(R.id.curedIncr_text),
                                R.color.cured);
                        ((TextView) foreignView.findViewById(R.id.curConfirmedCount_text))
                                .setText(addComma(global.currentConfirmedCount));
                        ((TextView) foreignView.findViewById(R.id.confirmedCount_text))
                                .setText(addComma(global.confirmedCount));
                        ((TextView) foreignView.findViewById(R.id.deathCount_text))
                                .setText(addComma(global.deadCount));
                        ((TextView) foreignView.findViewById(R.id.curedCount_text))
                                .setText(addComma(global.curedCount));
                        FrameLayout overallContainer_foreign = view.findViewById(R.id.overall_container_foreign);
                        overallContainer_foreign.addView(foreignView);

                        LinearLayout itemContainer1 = view.findViewById(R.id.overview_domestic_container);
                        LinearLayout itemContainer2 = view.findViewById(R.id.overview_foreign_container);

                        for (OverviewProvince province : provinceList.subList(0, OVERVIEW_ITEM_COUNT)) {
                            addItem(province.provinceName, province.currentConfirmedCount, province.confirmedCount,
                                    province.deadCount, province.curedCount, itemContainer1);
                        }
                        for (OverviewCountry country : countryList.subList(0, OVERVIEW_ITEM_COUNT)) {
                            addItem(country.countryName, country.currentConfirmedCount, country.confirmedCount,
                                    country.deadCount, country.curedCount, itemContainer2);
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void setTextViewText(String increment, TextView textView, int colorId) {
        if (increment.equals("0")) {
            textView.setText("较昨日无变化");
        } else {
            String text;
            if (Integer.valueOf(increment) > 0) {
                text = "较昨日+" + addComma(increment);
            } else {
                text = "较昨日" + addComma(increment);
            }

            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(colorId));
            spannableString.setSpan(colorSpan, 3, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textView.setText(spannableString);
        }
    }

    private void addItem(String areaName, String curConfirmedCount, String confirmedCount,
                         String deathCount, String curedCount, LinearLayout itemContainer) {
        View item = LayoutInflater.from(context)
                .inflate(R.layout.fragment_overview_list_item, null);

        ((TextView) item.findViewById(R.id.overview_area_text)).setText(areaName);
        ((TextView) item.findViewById(R.id.overview_curConfirmedCount_text)).setText(curConfirmedCount);
        ((TextView) item.findViewById(R.id.overview_confirmedCount_text)).setText(confirmedCount);
        ((TextView) item.findViewById(R.id.overview_death_text)).setText(deathCount);
        ((TextView) item.findViewById(R.id.overview_cured_text)).setText(curedCount);

        itemContainer.addView(item);
    }

    private String addComma(String string) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(string));
    }
}
