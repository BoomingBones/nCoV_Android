package com.boomingbones.ncov_mvvm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.boomingbones.ncov_mvvm.bean.Area;

import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Area> {

    private int resourceId;

    public ListViewAdapter(Context context, int textViewResourceId, List<Area> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Area area = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameText = view.findViewById(R.id.overview_area_text);
            viewHolder.curConfirmedText = view.findViewById(R.id.overview_curConfirmedCount_text);
            viewHolder.confirmedText = view.findViewById(R.id.overview_confirmedCount_text);
            viewHolder.deadText = view.findViewById(R.id.overview_death_text);
            viewHolder.curedText = view.findViewById(R.id.overview_cured_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.nameText.setText(area.areaName);
        viewHolder.curConfirmedText.setText(addComma(area.currentConfirmedCount));
        viewHolder.confirmedText.setText(addComma(area.confirmedCount));
        viewHolder.curedText.setText(addComma(area.curedCount));
        viewHolder.deadText.setText(addComma(area.deadCount));

        return view;
    }

    private String addComma(String string) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(string));
    }

    class ViewHolder {
        TextView nameText;
        TextView curConfirmedText;
        TextView confirmedText;
        TextView curedText;
        TextView deadText;
    }
}