package com.boomingbones.ncov;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private static final int NEWS_ITEM_COUNT = 10;

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

        for (int i = 0; i < NEWS_ITEM_COUNT; i++) {
            addItem(Data.newsPubTimeList[i], Data.newsTitleList[i], Data.newsContentList[i],
                    Data.newsInfoSourceList[i], Data.newsSourceUrlList[i]);
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void addItem(String time, String title, String content,
                         String infoSource, String sourceUrl) {
        long timeStamp = Long.parseLong(time);
        String leftTime = String.valueOf((System.currentTimeMillis() - timeStamp) / 3600000);

        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_news_item, null);
        View cardView = item.findViewById(R.id.news_cardView);
        cardView.setTag(sourceUrl);
        cardView.setOnClickListener(new CardViewClickListener());

        ((TextView) item.findViewById(R.id.news_leftTime_text)).setText(leftTime + "小时前");
        ((TextView) item.findViewById(R.id.news_title_text)).setText(title);
        ((TextView) item.findViewById(R.id.news_content_text)).setText("        " + content);
        ((TextView) item.findViewById(R.id.news_source_text)).setText(infoSource);

        itemContainer.addView(item);
    }

    class CardViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(v.getTag().toString()));
            startActivity(intent);
        }
    }
}
