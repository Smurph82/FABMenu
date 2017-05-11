/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;

/**
 * Created by Ben Murphy on 5/9/2017.
 *
 * This is a build class for putting together the objects needed for the {@link FABMenu}
 */

public class FABMenuBuilder {

    /** The custom X point used to get the FAB menu int the correct position on the screen */
    private int xOffset = -1;

    /** The custom Y point used to get the FAB menu int the correct position on the screen */
    private int yOffset = -1;

    /** The {@link MenuRes} id of the menu xml file. */
    private @MenuRes int menuRes = -1;

    /** The orientation of the menu. Default is {@link FABMenu#VERTICAL} */
    private @FABMenu.OrientationDef int orientation = FABMenu.VERTICAL;

    /** The click listener for the items in the FAB menu */
    private OnFABMenuItemClickListener l;

    /** Used to customize the FAB menu such as color of icon backgrounds. */
    private @Nullable FABMenuCustomCallback callback;

    /** Blank constructor */
    public FABMenuBuilder() { }

    /** @return The menu xml file id to load. */
    @MenuRes public int getMenuRes() { return menuRes; }

    /** @param menuRes The menu xml id to use with the FAB menu */
    public FABMenuBuilder setMenuRes(int menuRes) { this.menuRes = menuRes; return this; }

    /** @return The orientation of the FAB menu. {@link FABMenu#VERTICAL} is default. */
    public int getOrientation() { return orientation; }

    /** @param orientation The orientation of the FAB menu. */
    public FABMenuBuilder setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    /** @return The instance of {@code OnFABMenuItemClickListener} that you are using. */
    public OnFABMenuItemClickListener getListener() { return l; }

    /**
     * @param l The isntance of the {@code OnFABMenuItemClickListener} you created to get
     *          callbacks on.
     */
    public FABMenuBuilder setListener(OnFABMenuItemClickListener l) {
        this.l = l;
        return this;
    }

    /** @return The instance of {@code FABMenuCustomCallback} used to customize the FAB menu.  */
    @Nullable public FABMenuCustomCallback getCallback() { return callback; }

    /**
     * @param callback The instance of {@code FABMenuCustomCallback} for interacting with the
     *                 FAB menu.
     */
    public FABMenuBuilder setCallback(@Nullable FABMenuCustomCallback callback) {
        this.callback = callback;
        return this;
    }

    /** @return The custom x offset */
    public int getXOffset() { return xOffset; }

    /** @param xOffset The custom x offset point*/
    public FABMenuBuilder setXOffset(int xOffset) { this.xOffset = xOffset; return this; }

    /** @return The custom y offset*/
    public int getYOffset() { return yOffset; }

    /** @param yOffset The custom y offset point*/
    public FABMenuBuilder setYOffset(int yOffset) { this.yOffset = yOffset; return this; }
}
