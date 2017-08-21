/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * Created by Ben Murphy on 5/11/2017.
 *
 * Created so that you do not have to implement all the methods.
 */

public class SimpleFABMenuCustomCallback implements FABMenuCustomCallback {

    @Nullable @Override
    public SparseIntArray getMenuItemColors() { return null; }

    @Override public void startFABIconAnimation(boolean isOpening) { }

    @Override public boolean useCustomViews() { return false; }

    @Nullable
    @Override
    public ViewGroup inflateCustomView(@NonNull LayoutInflater inflater,
                                       @NonNull ViewGroup parent,
                                       @NonNull MenuItem item) {
        return null;
    }
}
