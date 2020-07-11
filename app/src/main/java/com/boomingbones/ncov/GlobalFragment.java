package com.boomingbones.ncov;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boomingbones.ncov.struct.Area;
import com.boomingbones.ncov.struct.Global;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFragment extends Fragment {


    private static final int OVERVIEW_ITEM_COUNT = 15;

    private Global globalData;
    private ArrayList<Area> areaList;

    private Context context;

    GlobalFragment(Global globalData, ArrayList<Area> areaList, Context context) {
        // Required empty public constructor
        this.globalData = globalData;
        this.areaList = areaList;
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global, container, false);

        view.findViewById(R.id.viewAll_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), ListViewActivity.class);

                bundle.putSerializable("area_data", areaList);
                intent.putExtra("title", R.string.overall_global);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
        String[] globalIncrValue = {
                globalData.currentConfirmedIncr, globalData.confirmedIncr,
                globalData.deadIncr, globalData.curedIncr};
        String[] globalCountValue = {
                globalData.currentConfirmedCount, globalData.confirmedCount,
                globalData.deadCount, globalData.curedCount};

        for (int i = 0; i < 4; i++) {
            ((TextView) view.findViewById(countTextViewId[i]))
                    .setText(addComma(globalCountValue[i]));
            if (globalIncrValue[i] != null) {
                setTextViewText(globalIncrValue[i], colorId[i],
                        (TextView) view.findViewById(incrTextViewId[i]));
            }
        }

        LinearLayout itemContainer = view.findViewById(R.id.overview_item_container);
        for (Area country : areaList.subList(0, OVERVIEW_ITEM_COUNT)) {
            addItem(country.areaName,
                    addComma(country.currentConfirmedCount),
                    addComma(country.confirmedCount),
                    addComma(country.deadCount),
                    addComma(country.curedCount),
                    itemContainer);
        }

        // Inflate the layout for this fragment
        return view;
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
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                    getResources().getColor(colorId));
            spannableString.setSpan(colorSpan, 3, text.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
