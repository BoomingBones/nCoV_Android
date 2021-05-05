package com.boomingbones.ncov_mvvm.ui.information;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.boomingbones.ncov_mvvm.R;

public class InformationFragment extends Fragment {

//    private InformationViewModel informationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        informationViewModel =
//                new ViewModelProvider(this).get(InformationViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_information, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        informationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
        View root = inflater.inflate(R.layout.fragment_information, container, false);

        View[] cardViewList = {
                root.findViewById(R.id.information_cardView1),
                root.findViewById(R.id.information_cardView2),
                root.findViewById(R.id.information_cardView3),
                root.findViewById(R.id.information_cardView4),
                root.findViewById(R.id.information_cardView5),
                root.findViewById(R.id.information_cardView6),
                root.findViewById(R.id.information_cardView7)};
        for (View cardView : cardViewList) {
            cardView.setOnClickListener(new CardViewClickListener());
        }

        return root;
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