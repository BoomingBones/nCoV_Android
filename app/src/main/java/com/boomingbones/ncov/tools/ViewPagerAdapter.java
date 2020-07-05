package com.boomingbones.ncov.tools;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.boomingbones.ncov.OverviewAreaFragment;
import com.boomingbones.ncov.struct.Area;
import com.boomingbones.ncov.struct.Domestic;
import com.boomingbones.ncov.struct.Global;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int MODE_DOMESTIC = 1;
    private static final int MODE_GLOBAL = 2;

    private Domestic domestic;
    private Global global;
    private List<Area> provinceList;
    private List<Area> countryList;
    private Context context;

    private String[] title = {"全国疫情", "全球疫情"};

    public ViewPagerAdapter(@NonNull FragmentManager fm, Domestic domestic, Global global,
                            List<Area> provinceList, List<Area> countryList, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.domestic = domestic;
        this.global = global;
        this.provinceList = provinceList;
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new OverviewAreaFragment(global, countryList, context, MODE_GLOBAL);
        } else {
            return new OverviewAreaFragment(domestic, provinceList, context, MODE_DOMESTIC);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
