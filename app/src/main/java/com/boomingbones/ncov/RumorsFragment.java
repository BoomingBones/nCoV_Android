package com.boomingbones.ncov;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RumorsFragment extends Fragment {

    private static final int RUMORS_ITEM_COUNT = 5;

    private View view;
    private Context context;
    private LinearLayout itemContainer;

    public RumorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rumors, container, false);
        context = getContext();
        itemContainer = view.findViewById(R.id.rumors_container);

        initFragment();

        return view;
    }

    private void initFragment() {
        for (int i = 0; i < RUMORS_ITEM_COUNT; i++) {
            addItem(i + 1, Data.rumorsTitleList[i],
                    Data.rumorsSummaryList[i], Data.rummorContentList[i]);
        }
    }

    private void addItem(int number, String title, String summary, String content) {
        View item = LayoutInflater.from(context)
                .inflate(R.layout.fragment_rumors_item, null);

        if (number == 1) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            int px = dp2px(10);
            params.setMargins(px, px, px, px);
            item.findViewById(R.id.rumors_cardView).setLayoutParams(params);
        }
        ((TextView) item.findViewById(R.id.rumors_number_text)).setText(String.valueOf(number));
        ((TextView) item.findViewById(R.id.rumors_title_text)).setText(title);
        ((TextView) item.findViewById(R.id.rumors_summary_text)).setText(summary);
        ((TextView) item.findViewById(R.id.rumors_content_text)).setText("        " + content);

        itemContainer.addView(item);
    }

    private int dp2px(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
