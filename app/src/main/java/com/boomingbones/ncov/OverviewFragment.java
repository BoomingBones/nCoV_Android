package com.boomingbones.ncov;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

    private static final int OVERVIEW_ITEM_COUNT = 15;
    private ArrayList<OverviewArea> provinceList;
    private ArrayList<OverviewArea> countryList;

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

        view.findViewById(R.id.viewAll_provinces).setOnClickListener(new ViewAllClickListener());
        view.findViewById(R.id.viewAll_countries).setOnClickListener(new ViewAllClickListener());

        initFragment(container);

        return view;
    }

    private void initFragment(final ViewGroup container) {
        swipeRefreshLayout.setRefreshing(true);

        final String address = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
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
                countryList = gson.fromJson(jsonString, new TypeToken<List<OverviewArea>>(){}.getType());

                matcher = Pattern.compile("(?<=Stat = ).*?(?=\\}catch)").matcher(responseString);
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                provinceList = gson.fromJson(jsonString, new TypeToken<List<OverviewArea>>(){}.getType());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View domesticView = LayoutInflater.from(context).inflate(
                                R.layout.fragment_overview_domestic, container, false);
                        View globalView = LayoutInflater.from(context).inflate(
                                R.layout.fragment_overview_global, container, false);

                        int[] incrTextViewId = {
                                R.id.curConfirmedIncr_text, R.id.confirmedIncr_text,
                                R.id.deathIncr_text, R.id.curedIncr_text,
                                R.id.importedIncr_text, R.id.suspectedIncr_text};
                        int[] countTextViewId = {
                                R.id.curConfirmedCount_text, R.id.confirmedCount_text,
                                R.id.deathCount_text, R.id.curedCount_text,
                                R.id.importedCount_text, R.id.suspectedCount_text};
                        int[] colorId = {
                                R.color.curConfirmed, R.color.confirmed, R.color.death,
                                R.color.cured, R.color.imported, R.color.suspected};
                        String[] domesticIncrValue = {
                                domestic.currentConfirmedIncr, domestic.confirmedIncr,
                                domestic.deadIncr, domestic.curedIncr,
                                domestic.importedIncr, domestic.suspectIncr};
                        String[] domesticCountValue = {
                                domestic.currentConfirmedCount, domestic.confirmedCount,
                                domestic.deadCount, domestic.curedCount,
                                domestic.importedCount, domestic.suspectCount};
                        String[] globalIncrValue = {
                                global.currentConfirmedIncr, global.confirmedIncr,
                                global.deadIncr, global.curedIncr};
                        String[] globalCountValue = {
                                global.currentConfirmedCount, global.confirmedCount,
                                global.deadCount, global.curedCount};

                        for (int i = 0; i < 6; i++) {
                            setTextViewText(domesticIncrValue[i], colorId[i],
                                    (TextView) domesticView.findViewById(incrTextViewId[i]));
                            ((TextView) domesticView.findViewById(countTextViewId[i]))
                                    .setText(addComma(domesticCountValue[i]));
                        }
                        ((FrameLayout) view.findViewById(R.id.overall_container_domestic)).addView(domesticView);

                        for (int i = 0; i < 4; i++) {
                            setTextViewText(globalIncrValue[i], colorId[i],
                                    (TextView) globalView.findViewById(incrTextViewId[i]));
                            ((TextView) globalView.findViewById(countTextViewId[i]))
                                    .setText(addComma(globalCountValue[i]));
                        }
                        ((FrameLayout) view.findViewById(R.id.overall_container_global)).addView(globalView);

                        LinearLayout itemContainer1 = view.findViewById(R.id.overview_domestic_container);
                        LinearLayout itemContainer2 = view.findViewById(R.id.overview_foreign_container);

                        for (OverviewArea province : provinceList.subList(0, OVERVIEW_ITEM_COUNT)) {
                            addItem(province.areaName,
                                    addComma(province.currentConfirmedCount),
                                    addComma(province.confirmedCount),
                                    addComma(province.deadCount),
                                    addComma(province.curedCount),
                                    itemContainer1);
                        }
                        for (OverviewArea country : countryList.subList(0, OVERVIEW_ITEM_COUNT)) {
                            addItem(country.areaName,
                                    addComma(country.currentConfirmedCount),
                                    addComma(country.confirmedCount),
                                    addComma(country.deadCount),
                                    addComma(country.curedCount),
                                    itemContainer2);
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void setTextViewText(String increment, int colorId, TextView textView) {
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

    class ViewAllClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), ListViewActivity.class);

            if (v.getId() == R.id.viewAll_provinces) {
                bundle.putSerializable("area_data", provinceList);
                intent.putExtra("title", R.string.overall_domestic);
            } else {
                bundle.putSerializable("area_data", countryList);
                intent.putExtra("title", R.string.overall_global);
            }

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
