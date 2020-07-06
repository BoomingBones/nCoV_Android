package com.boomingbones.ncov;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boomingbones.ncov.struct.News;
import com.boomingbones.ncov.tools.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private static final int NEWS_ITEM_COUNT = 10;
    private List<News> newsList;

    private Handler handler;
    private View view;
    private Context context;
    private LinearLayout itemContainer;

    public NewsFragment(Handler handler) {
        // Required empty public constructor
        this.handler = handler;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        itemContainer = view.findViewById(R.id.news_container);
        context = getContext();

        initFragment();

        // Inflate the layout for this fragment
        return view;
    }

    private void initFragment() {
        String address = "https://lab.isaaclin.cn/nCoV/api/news";
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                JsonObject object = new JsonParser().parse(responseString).getAsJsonObject();
                JsonElement element = object.get("results");
                Gson gson = new Gson();
                newsList = gson.fromJson(element, new TypeToken<List<News>>(){}.getType());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int count = 1;
                        for (News news : newsList) {
                            addItem(count, news.pubTime, news.title, news.content, news.infoSource, news.sourceUrl);
                            count++;
                        }
                    }
                });

                Message message = new Message();
                message.what = 10002;
                handler.sendMessage(message);
            }
        });
    }

    private void addItem(int number, String time, String title, String content,
                          String infoSource, String sourceUrl) {
        String timeUnit = "分钟前";
        long timeStamp = Long.parseLong(time);
        long leftTime = (System.currentTimeMillis() - timeStamp) / 60000;
        if (leftTime >= 60) {
            leftTime /= 60;
            timeUnit = "小时前";
        }

        View item = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_news_item, null);
        View cardView = item.findViewById(R.id.news_cardView);
        cardView.setTag(sourceUrl);
        cardView.setOnClickListener(new CardViewClickListener());

        if (number == 1) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            float scale = context.getResources().getDisplayMetrics().density;
            int px = (int) (10 * scale + 0.5f);
            params.setMargins(px, px, px, px / 2);
            item.findViewById(R.id.news_cardView).setLayoutParams(params);
        }

        ((TextView) item.findViewById(R.id.news_leftTime_text)).setText(leftTime + timeUnit);
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
