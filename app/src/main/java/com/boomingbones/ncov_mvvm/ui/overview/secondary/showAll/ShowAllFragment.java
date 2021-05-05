package com.boomingbones.ncov_mvvm.ui.overview.secondary.showAll;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boomingbones.ncov_mvvm.ListViewAdapter;
import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Area;

import java.util.List;

public class ShowAllFragment extends Fragment {

    private Bundle bundle;
    private ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        View root = inflater.inflate(R.layout.fragment_showall, container, false);
        listView = root.findViewById(R.id.listView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int titleId = bundle.getInt("id");
        List<Area> areaList = (List<Area>) bundle.getSerializable("list");
        ((Toolbar) requireActivity().findViewById(R.id.toolbar)).setTitle(titleId);
        ListViewAdapter adapter = new ListViewAdapter(
                getContext(), R.layout.item_overview, areaList);
        listView.setAdapter(adapter);
    }
}