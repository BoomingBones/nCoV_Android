package com.boomingbones.ncov_mvvm.ui.overview.secondary;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Area;
import com.boomingbones.ncov_mvvm.bean.Domestic;
import com.boomingbones.ncov_mvvm.bean.Global;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class SecondaryFragment extends Fragment {

    public static final int OVERVIEW_ITEM_COUNT = 15;

    public Domestic domesticData;
    public Global globalData;
    public List<Area> areaList;

    public int[] incrTextViewId = {
            R.id.curConfirmedIncr_text, R.id.confirmedIncr_text,
            R.id.deathIncr_text, R.id.curedIncr_text,
            R.id.importedIncr_text, R.id.suspectedIncr_text};
    public int[] countTextViewId = {
            R.id.curConfirmedCount_text, R.id.confirmedCount_text,
            R.id.deathCount_text, R.id.curedCount_text,
            R.id.importedCount_text, R.id.suspectedCount_text};
    public int[] colorId = {
            R.color.curConfirmed, R.color.confirmed, R.color.death,
            R.color.cured, R.color.imported, R.color.suspected};

    public SecondaryFragment(Domestic domesticData, List<Area> areaList) {
        this.domesticData = domesticData;
        this.areaList = areaList;
    }

    public SecondaryFragment(Global globalData, List<Area> areaList) {
        this.globalData = globalData;
        this.areaList = areaList;
    }

    public final void setTextViewText(String increment, int colorId, TextView textView) {
        if (increment.equals("0")) {
            textView.setText("较昨日无变化");
        } else {
            String text;
            if (Integer.parseInt(increment) > 0) {
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

    public final void addItem(String areaName, String curConfirmedCount, String confirmedCount,
                         String deathCount, String curedCount, LinearLayout itemContainer) {
        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.item_overview, null);

        ((TextView) item.findViewById(R.id.overview_area_text)).setText(areaName);
        ((TextView) item.findViewById(R.id.overview_curConfirmedCount_text)).setText(curConfirmedCount);
        ((TextView) item.findViewById(R.id.overview_confirmedCount_text)).setText(confirmedCount);
        ((TextView) item.findViewById(R.id.overview_death_text)).setText(deathCount);
        ((TextView) item.findViewById(R.id.overview_cured_text)).setText(curedCount);

        itemContainer.addView(item);
    }

    public final String addComma(String string) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(string));
    }

    public final class ShowAllButtonClickListener implements View.OnClickListener {
        private final List<Area> areaList;
        private final int titleId;

        public ShowAllButtonClickListener(List<Area> areaList, int titleId) {
            this.areaList = areaList;
            this.titleId = titleId;
        }

//        @Override
//        public void onClick(View view) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("area_list", (Serializable) areaList);
//
//            Intent intent = new Intent(getActivity(), ListViewActivity.class);
//            intent.putExtra("title", titleId);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", titleId);
            bundle.putSerializable("list", (Serializable) areaList);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_nav_overview_to_nav_showAll, bundle);
        }
    }
}
