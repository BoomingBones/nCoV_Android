package com.boomingbones.ncov_mvvm.ui.rumors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Rumor;

import java.util.List;

public class RumorsFragment extends Fragment {

    private RumorsViewModel rumorsViewModel;
    private RecyclerView itemContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rumors, container, false);
        itemContainer = root.findViewById(R.id.rumors_container);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(
                this, new RumorsViewModelFactory(requireContext().getAssets()));
        rumorsViewModel = provider.get(RumorsViewModel.class);
        rumorsViewModel.getRumorsData().observe(getViewLifecycleOwner(), new Observer<List<Rumor>>() {
            @Override
            public void onChanged(List<Rumor> rumorList) {
                itemContainer.setLayoutManager(new LinearLayoutManager(getContext()));
                itemContainer.setAdapter(new RecyclerViewAdapter(rumorList));
            }
        });
    }
}