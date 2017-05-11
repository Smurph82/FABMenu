/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.View;

/**
 * Created by Ben Murphy on 5/11/2017.
 *
 * Interface used to customize the FAB menu
 */

public interface FABMenuCustomCallback {

    /**
     * @return Called to return a {@code SparseIntArray} that contains the menu item id as
     * the <em>key</em> and the {@link ColorRes} as the value. Return null if you do not want
     * custom colors for the mini FABs
     */
    @Nullable
    SparseIntArray getMenuItemColors();

    /**
     * Called right before the {@link FABMenu#animateWindowInCircular(View, View)}.
     * This way you could use an AnimatedVectorDrawable with your FAB if you want.
     */
    void startFABIconAnimation();
}
