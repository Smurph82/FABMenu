/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.IdRes;

/**
 * Created by Ben Murphy on 5/11/2017.
 *
 * Created so that you do not have to implement all the methods.
 */

public class SimpleOnFABMenuItemClickListener implements OnFABMenuItemClickListener {
    @Override public void onItemClicked(@IdRes int id) { }

    @Override public boolean onItemLongClicked(@IdRes int id) { return false; }
}
