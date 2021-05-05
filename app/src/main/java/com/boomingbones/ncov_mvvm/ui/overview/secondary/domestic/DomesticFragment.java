package com.boomingbones.ncov_mvvm.ui.overview.secondary.domestic;

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
import com.boomingbones.ncov_mvvm.bean.Domestic;
import com.boomingbones.ncov_mvvm.ui.overview.secondary.SecondaryFragment;

import java.util.List;

public class DomesticFragment extends SecondaryFragment {

    public DomesticFragment(Domestic domesticData, List<Area> areaList) {
        super(domesticData, areaList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_domestic, container, false);
        root.findViewById(R.id.showAll_text).setOnClickListener(
                new ShowAllButtonClickListener(areaList, R.string.overall_domestic));

        String[] domesticIncrValue = {
                domesticData.currentConfirmedIncr, domesticData.confirmedIncr,
                domesticData.deadIncr, domesticData.curedIncr,
                domesticData.importedIncr, domesticData.suspectIncr};
        String[] domesticCountValue = {
                domesticData.currentConfirmedCount, domesticData.confirmedCount,
                domesticData.deadCount, domesticData.curedCount,
                domesticData.importedCount, domesticData.suspectCount};

        for (int i = 0; i < 6; i++) {
            ((TextView) root.findViewById(countTextViewId[i]))
                    .setText(addComma(domesticCountValue[i]));
            if (domesticIncrValue[i] != null) {
                setTextViewText(domesticIncrValue[i], colorId[i],
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