package com.boomingbones.ncov;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    private View view;
    private Handler handler;

    public InformationFragment(Handler handler) {
        // Required empty public constructor
        this.handler = handler;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_information, container, false);

        View[] cardViewList = {
                view.findViewById(R.id.information_cardView1),
                view.findViewById(R.id.information_cardView2),
                view.findViewById(R.id.information_cardView3),
                view.findViewById(R.id.information_cardView4),
                view.findViewById(R.id.information_cardView5),
                view.findViewById(R.id.information_cardView6),
                view.findViewById(R.id.information_cardView7)};
        for (View cardView : cardViewList) {
            cardView.setOnClickListener(new CardViewClickListener());
        }

        Message message = new Message();
        message.what =10002;
        handler.sendMessage(message);

        return view;
    }

    class CardViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(v.getTag().toString()));
            startActivity(intent);
        }
    }
}
