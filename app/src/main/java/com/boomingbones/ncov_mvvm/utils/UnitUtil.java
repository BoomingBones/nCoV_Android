package com.boomingbones.ncov_mvvm.utils;

import android.content.Context;

public class UnitUtil {

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
