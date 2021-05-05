package com.boomingbones.ncov_mvvm.ui.rumors;

import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RumorsViewModelFactory implements ViewModelProvider.Factory {

    private AssetManager assetManager;

    public RumorsViewModelFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RumorsViewModel(assetManager);
    }
}
