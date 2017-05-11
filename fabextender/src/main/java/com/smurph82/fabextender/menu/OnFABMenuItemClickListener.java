/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.IdRes;

/**
 * Created by Ben Murphy on 5/11/2017.
 *
 * Interface for the FAB menu items on click listener.
 */

public interface OnFABMenuItemClickListener {
    /**
     * Called when the menu item is clicked.
     *
     * @param id The menu id of the item that was clicked
     */
    void onItemClicked(@IdRes int id);

    /**
     * Called when the menu item was long clicked
     *
     * @param id The menu id of the item that was long clicked
     * @return {@code true} if the click was handled, {@code false} if it was not.
     */
    boolean onItemLongClicked(@IdRes int id);
}
