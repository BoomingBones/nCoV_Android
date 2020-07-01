package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private static final int OVERVIEW_ITEM_COUNT = 15;

    private View view;
    private Context context;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_overview, container, false);
        context = getContext();

        initFragment(container);

        return view;
    }

    private void initFragment(ViewGroup container) {
        View domestic = LayoutInflater.from(context)
                .inflate(R.layout.fragment_overview_overall_domestic, container, false);
        setTextViewText(DataStatic.overallDomesticCurConfirmedCount_incr,
                (TextView) domestic.findViewById(R.id.curConfirmedIncr_text),
                R.color.curConfirmed);
        setTextViewText(DataStatic.overallDomesticImportedCount_incr,
                (TextView) domestic.findViewById(R.id.importedIncr_text),
                R.color.imported);
        setTextViewText(DataStatic.overallDomesticSuspectedCount_incr,
                (TextView) domestic.findViewById(R.id.suspectedIncr_text),
                R.color.suspected);
        setTextViewText(DataStatic.overallDomesticConfirmedCount_incr,
                (TextView) domestic.findViewById(R.id.confirmedIncr_text),
                R.color.confirmed);
        setTextViewText(DataStatic.overallDomesticDeadCount_incr,
                (TextView) domestic.findViewById(R.id.deathIncr_text),
                R.color.death);
        setTextViewText(DataStatic.overallDomesticCuredCount_incr,
                (TextView) domestic.findViewById(R.id.curedIncr_text),
                R.color.cured);
        ((TextView) domestic.findViewById(R.id.curConfirmedCount_text))
                .setText(addComma(DataStatic.overallDomesticCurConfirmedCount));
        ((TextView) domestic.findViewById(R.id.importedCount_text))
                .setText(addComma(DataStatic.overallDomesticImportedCount));
        ((TextView) domestic.findViewById(R.id.suspectedCount_text))
                .setText(addComma(DataStatic.overallDomesticSuspectedCount));
        ((TextView) domestic.findViewById(R.id.confirmedCount_text))
                .setText(addComma(DataStatic.overallDomesticConfirmedCount));
        ((TextView) domestic.findViewById(R.id.deathCount_text))
                .setText(addComma(DataStatic.overallDomesticDeadCount));
        ((TextView) domestic.findViewById(R.id.curedCount_text))
                .setText(addComma(DataStatic.overallDomesticCuredCount));
        FrameLayout overallContainer_domestic = view.findViewById(R.id.overall_container_domestic);
        overallContainer_domestic.addView(domestic);

        View foreign = LayoutInflater.from(context)
                .inflate(R.layout.fragment_overview_overall_foreign, container, false);
        setTextViewText(DataStatic.overallForeignCurConfirmedCount_incr,
                (TextView) foreign.findViewById(R.id.curConfirmedIncr_text),
                R.color.curConfirmed);
        setTextViewText(DataStatic.overallForeignConfirmedCount_incr,
                (TextView) foreign.findViewById(R.id.confirmedIncr_text),
                R.color.confirmed);
        setTextViewText(DataStatic.overallForeignDeadCount_incr,
                (TextView) foreign.findViewById(R.id.deathIncr_text),
                R.color.death);
        setTextViewText(DataStatic.overallForeignCuredCount_incr,
                (TextView) foreign.findViewById(R.id.curedIncr_text),
                R.color.cured);
        ((TextView) foreign.findViewById(R.id.curConfirmedCount_text))
                .setText(addComma(DataStatic.overallForeignCurConfirmedCount));
        ((TextView) foreign.findViewById(R.id.confirmedCount_text))
                .setText(addComma(DataStatic.overallForeignConfirmedCount));
        ((TextView) foreign.findViewById(R.id.deathCount_text))
                .setText(addComma(DataStatic.overallForeignDeadCount));
        ((TextView) foreign.findViewById(R.id.curedCount_text))
                .setText(addComma(DataStatic.overallForeignCuredCount));
        FrameLayout overallContainer_foreign = view.findViewById(R.id.overall_container_foreign);
        overallContainer_foreign.addView(foreign);

        String[][] tempDataList1 = {
                Arrays.copyOf(DataStatic.domesticProvinceNameList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.domesticCurConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.domesticConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.domesticDeadCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.domesticCuredCountList, OVERVIEW_ITEM_COUNT)};
        String[][] tempDataList2 = {
                Arrays.copyOf(DataStatic.foreignCountryNameList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.foreignCurConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.foreignConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.foreignDeadCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(DataStatic.foreignCuredCountList, OVERVIEW_ITEM_COUNT)};
        LinearLayout itemContainer1 = view.findViewById(R.id.overview_domestic_container);
        LinearLayout itemContainer2 = view.findViewById(R.id.overview_foreign_container);

        for (int i = 0; i < OVERVIEW_ITEM_COUNT; i++) {
            addItem(tempDataList1[0][i], tempDataList1[1][i], tempDataList1[2][i],
                    tempDataList1[3][i], tempDataList1[4][i], itemContainer1);
            addItem(tempDataList2[0][i], tempDataList2[1][i], tempDataList2[2][i],
                    tempDataList2[3][i], tempDataList2[4][i], itemContainer2);
        }
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
