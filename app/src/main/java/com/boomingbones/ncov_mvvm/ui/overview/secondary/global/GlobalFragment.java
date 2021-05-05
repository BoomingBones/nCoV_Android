package com.boomingbones.ncov_mvvm.ui.overview.secondary.global;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Area;
import com.boomingbones.ncov_mvvm.bean.Global;
import com.boomingbones.ncov_mvvm.ui.overview.secondary.SecondaryFragment;

import java.util.List;

public class GlobalFragment extends SecondaryFragment {

    public GlobalFragment(Global globalData, List<Area> areaList) {
        super(globalData, areaList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_global, container, false);
        root.findViewById(R.id.showAll_text).setOnClickListener(
                new ShowAllButtonClickListener(areaList, R.string.overall_global));

        String[] globalIncrValue = {
                globalData.currentConfirmedIncr, globalData.confirmedIncr,
                globalData.deadIncr, globalData.curedIncr};
        String[] globalCountValue = {
                globalData.currentConfirmedCount, globalData.confirmedCount,
                globalData.deadCount, globalData.curedCount};

        for (int i = 0; i < 4; i++) {
            ((TextView) root.findViewById(countTextViewId[i]))
                    .setText(addComma(globalCountValue[i]));
            if (globalIncrValue[i] != null) {
                setTextViewText(globalIncrValue[i], colorId[i],
                        (TextView) root.findViewById(incrTextViewId[i]));
            }
        }

        LinearLayout itemContainer = root.findViewById(R.id.overview_item_container);
        for (Area country : areaList.subList(0, OVERVIEW_ITEM_COUNT)) {
            addItem(country.areaName,
                    addComma(country.currentConfirmedCount),
                    addComma(country.confirmedCount),
                    addComma(country.deadCount),
                    addComma(country.curedCount),
                    itemContainer);
        }

        return root;
    }
}