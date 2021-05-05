package com.boomingbones.ncov_mvvm.ui.overview;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boomingbones.ncov_mvvm.R;
import com.boomingbones.ncov_mvvm.bean.Overview;
import com.boomingbones.ncov_mvvm.ui.overview.secondary.domestic.DomesticFragment;
import com.boomingbones.ncov_mvvm.ui.overview.secondary.global.GlobalFragment;
import com.google.android.material.tabs.TabLayout;

public class OverviewFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        viewPager = root.findViewById(R.id.viewPager_overview);
        tabLayout = root.findViewById(R.id.overview_tabLayout);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OverviewViewModel overviewViewModel =
                new ViewModelProvider(this).get(OverviewViewModel.class);
        overviewViewModel.getOverviewData().observe(getViewLifecycleOwner(), new Observer<Overview>() {
            @Override
            public void onChanged(Overview overview) {
                Fragment[] fragments = {
                        new DomesticFragment(overview.domestic, overview.provincesList),
                        new GlobalFragment(overview.global, overview.countriesList)};
                String[] titles = {
                        getString(R.string.overview_domestic),
                        getString(R.string.overview_global)};

                ViewPagerAdapter adapter = new ViewPagerAdapter(
                        getChildFragmentManager(), fragments, titles);
                viewPager.setAdapter(adapter);

                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }
}