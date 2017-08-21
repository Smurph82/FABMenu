/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
     *
     * @param isOpening {@code true} if the menu is opening, {@code false} if the menu is closing.
     */
    void startFABIconAnimation(boolean isOpening);

    /**
     * @return {@code true} if you want to use custom views for the menu items,
     * {@code false} if not
     * */
    boolean useCustomViews();

    /**
     * Used to inflate custom views into the FAB menu.
     *
     * @param inflater An instance of the {@code LayoutInflater}
     * @param parent   The parent view to use with the inflater
     * @param item     The current menu item.
     * @return A ViewGroup of your custom view.
     */
    @Nullable ViewGroup inflateCustomView(@NonNull LayoutInflater inflater,
                                          @NonNull ViewGroup parent,
                                          @NonNull MenuItem item);
}
