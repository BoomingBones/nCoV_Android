package com.boomingbones.ncov;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private View view;
    private LinearLayout itemContainer;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        itemContainer = view.findViewById(R.id.news_container);

        for(int i = 0; i < 5; i++) {
            addItem(Data.pubTimeList[i], Data.titleList[i], Data.contentList[i], Data.sourceList[i]);
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void addItem(String time, String title, String content, String source) {
        long timeStamp = Long.parseLong(time);
        String leftTime = String.valueOf((System.currentTimeMillis() - timeStamp) / 3600000 - 24);

        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_news_item, null);
        ((TextView) item.findViewById(R.id.leftTime_text)).setText(leftTime + "小时前");
        ((TextView) item.findViewById(R.id.title_text)).setText(title);
        ((TextView) item.findViewById(R.id.content_text)).setText("        " + content);
        ((TextView) item.findViewById(R.id.source_text)).setText(source);
        itemContainer.addView(item);
    }

}
