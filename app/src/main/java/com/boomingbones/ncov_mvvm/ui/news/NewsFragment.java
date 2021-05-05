package com.boomingbones.ncov_mvvm.ui.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.News;
import com.boomingbones.ncov_mvvm.utils.UnitUtil;

import java.util.List;

public class NewsFragment extends Fragment {

    private Context context;
    private LinearLayout itemContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);
        context = getContext();
        itemContainer = root.findViewById(R.id.news_container);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        newsViewModel.getNewsData().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> newsList) {
                boolean isFirst = true;
                for (News news: newsList) {
                    String timeUnit = "分钟";
                    long timeStamp = Long.parseLong(news.pubTime);
                    int leftTime = (int) ((System.currentTimeMillis() - timeStamp) / 60000);
                    if (leftTime >= 60) {
                        leftTime /= 60;
                        timeUnit = "小时";
                    }

                    View item = getLayoutInflater().inflate(R.layout.item_news,
                            itemContainer, false);
                    View cardView = item.findViewById(R.id.cardView_news);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(news.sourceUrl));
                            startActivity(intent);
                        }
                    });

                    if (isFirst) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        int px = UnitUtil.dp2px(requireContext(), 10);
                        params.setMargins(px, px, px, px);
                        cardView.setLayoutParams(params);
                        isFirst = false;
                    }

                    String leftTimeText = getString(R.string.news_leftTime, leftTime, timeUnit);
                    ((TextView) item.findViewById(R.id.news_leftTime_text)).setText(leftTimeText);
                    ((TextView) item.findViewById(R.id.news_title_text)).setText(news.title);
                    String contentText = getString(R.string.news_content, news.content);
                    ((TextView) item.findViewById(R.id.news_content_text)).setText(contentText);
                    ((TextView) item.findViewById(R.id.news_source_text)).setText(news.infoSource);

                    itemContainer.addView(item);
                }
            }
        });
    }
}