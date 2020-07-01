package com.boomingbones.ncov;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout itemContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NewsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        itemContainer = view.findViewById(R.id.news_container);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        getData();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    for (News news : newsList) {
                        addItem(news.pubTime, news.title, news.content, news.infoSource, news.sourceUrl);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

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

    private void getData() {
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

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
    }
}
