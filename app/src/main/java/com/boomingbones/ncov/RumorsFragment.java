package com.boomingbones.ncov;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boomingbones.ncov.struct.Rumor;
import com.boomingbones.ncov.tools.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RumorsFragment extends Fragment {

    private static final int FRAGMENT_UPDATE_FINISH = 10002;
    private List<Rumor> rumorsList;

    private Context context;
    private LinearLayout itemContainer;
    private Handler handler;

    RumorsFragment(Handler handler) {
        // Required empty public constructor
        this.handler = handler;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rumors, container, false);
        context = getContext();
        itemContainer = view.findViewById(R.id.rumors_container);

        initFragment();

        return view;
    }

    private void addItem(int number, String title, String summary, String content, int type) {
        View item = LayoutInflater.from(context)
                .inflate(R.layout.fragment_rumors_item, null);
        if (number == 1) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            float scale = context.getResources().getDisplayMetrics().density;
            int px = (int) (10 * scale + 0.5f);
            params.setMargins(px, px, px, px);
            item.findViewById(R.id.rumors_cardView).setLayoutParams(params);
        }

        ((TextView) item.findViewById(R.id.rumors_number_text)).setText(String.valueOf(number));
        ((TextView) item.findViewById(R.id.rumors_title_text)).setText(title);
        ((TextView) item.findViewById(R.id.rumors_summary_text)).setText(summary);
        ((TextView) item.findViewById(R.id.rumors_content_text)).setText("        " + content);

        ImageView imageView = item.findViewById(R.id.rumors_type_image);
        if (type == 0) {
            imageView.setImageResource(R.drawable.img_rumors_false);
        } else if (type == 1) {
            imageView.setImageResource(R.drawable.img_rumors_true);
        } else {
            imageView.setImageResource(R.drawable.img_rumors_unknown);
        }

        itemContainer.addView(item);
    }

    private void initFragment() {

        String address = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                updateFinish(false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseString = response.body().string();
                Matcher matcher = Pattern.compile("(?<=RumorList = ).*?(?=\\}catch)").matcher(responseString);
                String jsonString = null;
                if (matcher.find()) {
                    jsonString = matcher.group();
                }
                Gson gson = new Gson();
                rumorsList = gson.fromJson(jsonString, new TypeToken<List<Rumor>>(){}.getType());

                if (rumorsList != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int count = 1;
                            for (Rumor rumor : rumorsList) {
                                addItem(count, rumor.title, rumor.summary, rumor.content, rumor.rumorType);
                                if (count == 5) {
                                    break;
                                }
                                count++;
                            }
                        }
                    });

                    updateFinish(true);
                }
                else {
                    updateFinish(false);
                }
            }
        });
    }

    private void updateFinish(boolean isSuccess) {
        if (!isSuccess) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Failure", Toast.LENGTH_LONG).show();
                }
            });
        }

        Message message = new Message();
        message.what = FRAGMENT_UPDATE_FINISH;
        handler.sendMessage(message);
    }
}
