package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.boomingbones.ncov.struct.Area;
import com.boomingbones.ncov.struct.Domestic;
import com.boomingbones.ncov.struct.Global;
import com.boomingbones.ncov.struct.Overall;

import java.text.DecimalFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewAreaFragment extends Fragment {

    private static final int OVERVIEW_ITEM_COUNT = 15;
    private static final int MODE_DOMESTIC = 1;
    private static final int MODE_GLOBAL = 2;

    private int mode;
    private Overall overall;
    private List<Area> areaList;

    private Context context;

    public OverviewAreaFragment(Overall overall, List<Area> areaList, Context context, int mode) {
        // Required empty public constructor
        this.overall = overall;
        this.areaList = areaList;
        this.mode = mode;

        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview_area, container, false);

        View domesticView = LayoutInflater.from(context).inflate(
                R.layout.fragment_overview_header_domestic, container, false);
        View globalView = LayoutInflater.from(context).inflate(
                R.layout.fragment_overview_header_global, container, false);

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

        if (mode == MODE_DOMESTIC) {
            Domestic domestic = (Domestic) overall;

            String[] domesticIncrValue = {
                    domestic.currentConfirmedIncr, domestic.confirmedIncr,
                    domestic.deadIncr, domestic.curedIncr,
                    domestic.importedIncr, domestic.suspectIncr};
            String[] domesticCountValue = {
                    domestic.currentConfirmedCount, domestic.confirmedCount,
                    domestic.deadCount, domestic.curedCount,
                    domestic.importedCount, domestic.suspectCount};

            for (int i = 0; i < 6; i++) {
                setTextViewText(domesticIncrValue[i], colorId[i],
                        (TextView) domesticView.findViewById(incrTextViewId[i]));
                ((TextView) domesticView.findViewById(countTextViewId[i]))
                        .setText(addComma(domesticCountValue[i]));
            }
            ((FrameLayout) view.findViewById(R.id.overall_container)).addView(domesticView);
        }

        else {
            Global global = (Global) overall;

            String[] globalIncrValue = {
                    global.currentConfirmedIncr, global.confirmedIncr,
                    global.deadIncr, global.curedIncr};
            String[] globalCountValue = {
                    global.currentConfirmedCount, global.confirmedCount,
                    global.deadCount, global.curedCount};

            for (int i = 0; i < 4; i++) {
                setTextViewText(globalIncrValue[i], colorId[i],
                        (TextView) globalView.findViewById(incrTextViewId[i]));
                ((TextView) globalView.findViewById(countTextViewId[i]))
                        .setText(addComma(globalCountValue[i]));
            }
            ((FrameLayout) view.findViewById(R.id.overall_container)).addView(globalView);
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
