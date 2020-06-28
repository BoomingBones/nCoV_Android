package com.boomingbones.ncov;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class RumorsFragment extends Fragment {

    private static final int RUMORS_ITEM_COUNT = 5;

    private View view;
    private LinearLayout itemContainer;

    public RumorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rumors, container, false);
        itemContainer = view.findViewById(R.id.rumors_container);

        for (int i = 0; i < RUMORS_ITEM_COUNT; i++) {
            addItem(i + 1, Data.rumorsTitleList[i],
                    Data.rumorsSummaryList[i], Data.rummorContentList[i]);
        }

        return view;
    }

    private void addItem(int number, String title, String summary, String content) {
        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_rumors_item, null);

        ((TextView) item.findViewById(R.id.rumors_number_text)).setText(String.valueOf(number));
        ((TextView) item.findViewById(R.id.rumors_title_text)).setText(title);
        ((TextView) item.findViewById(R.id.rumors_summary_text)).setText(summary);
        ((TextView) item.findViewById(R.id.rumors_content_text)).setText("        " + content);

        itemContainer.addView(item);
    }
}
