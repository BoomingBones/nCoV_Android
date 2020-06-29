package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private static final int OVERVIEW_ITEM_COUNT = 15;

    private View view;
    private Context context;
    private LinearLayout itemContainer_domestic;
    private LinearLayout itemContainer_foreign;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_overview, container, false);
        context = getContext();

        String[][] tempDataList1 = {
                Arrays.copyOf(Data.domesticProvinceNameList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.domesticCurConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.domesticConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.domesticDeadCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.domesticCuredCountList, OVERVIEW_ITEM_COUNT)};
        String[][] tempDataList2 = {
                Arrays.copyOf(Data.foreignCountryNameList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.foreignCurConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.foreignConfirmedCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.foreignDeadCountList, OVERVIEW_ITEM_COUNT),
                Arrays.copyOf(Data.foreignCuredCountList, OVERVIEW_ITEM_COUNT)};
        LinearLayout itemContainer1 = view.findViewById(R.id.overview_domestic_container);
        LinearLayout itemContainer2 = view.findViewById(R.id.overview_foreign_container);

        for (int i = 0; i < OVERVIEW_ITEM_COUNT; i++) {
            addItem(tempDataList1[0][i], tempDataList1[1][i], tempDataList1[2][i],
                    tempDataList1[3][i], tempDataList1[4][i], itemContainer1);
            addItem(tempDataList2[0][i], tempDataList2[1][i], tempDataList2[2][i],
                    tempDataList2[3][i], tempDataList2[4][i], itemContainer2);
        }

        return view;
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
}
