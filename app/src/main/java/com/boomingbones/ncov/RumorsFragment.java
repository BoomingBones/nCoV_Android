package com.boomingbones.ncov;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
public class RumorsFragment extends Fragment {

    private static final int RUMORS_ITEM_COUNT = 5;
    private List<Rumor> rumorsList;
    private Handler handler;

    private View view;
    private Context context;
    private LinearLayout itemContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RumorsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rumors, container, false);
        context = getContext();
        itemContainer = view.findViewById(R.id.rumors_container);
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
                if (msg.what == 2) {
                    int count = 1;
                    for (Rumor rumor : rumorsList) {
                        addItem(count, rumor.title, rumor.summary,rumor.content);
                        count++;
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

        return view;
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

    private void getData() {
        String address = "https://lab.isaaclin.cn/nCoV/api/rumors?num=5";
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
                rumorsList = gson.fromJson(element, new TypeToken<List<Rumor>>(){}.getType());

                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
    }
}
