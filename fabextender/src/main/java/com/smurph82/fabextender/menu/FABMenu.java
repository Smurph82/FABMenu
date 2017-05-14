/*
 * Copyright (C) 2017 Benjamin Murphy to Present.
 * All rights reserved.
 */

package com.smurph82.fabextender.menu;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Pair;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smurph82.fabextender.R;
import com.smurph82.fabextender.component.CircleColorImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Ben Murphy on 4/21/17.
 *
 * Use if you want to create a simple FAB menu from a menu.xml file.
 */

@SuppressWarnings("WeakerAccess")
public class FABMenu extends PopupWindow {

    /** The length of the overall animation duration. Default is 250 */
    private static final int ANIMATION_DURATION = 250;

    private WeakReference<Context> weakContext;
    private int orientation = VERTICAL;

    @Nullable private View currentAnchor;
    private ArrayList<View> childrenViews = new ArrayList<>(0);
    private LinearLayout rootLinearLayout;

    @Retention(RetentionPolicy.CLASS)
    @IntDef({VERTICAL, HORIZONTAL})
    public @interface OrientationDef{}
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    /** Given instance of the listener. */
    private OnFABMenuItemClickListener listener;

    /** Given instance of the callback */
    private FABMenuCustomCallback callback;

    /** The builder used to create this instance of the FABMenu */
    private FABMenuBuilder builder;

    /** {@code true} if the FAB menu is showing/open, {@code false} if not. */
    private boolean isMenuOpen = false;

    /**
     * The constructor for the {@code FABMenu} object
     *
     * @param context The current {@code Context} of the app.
     * @param builder The {@code FABMenuBuilder} used to build the parts of the FAB menu.
     */
    public FABMenu(@NonNull Context context, @NonNull FABMenuBuilder builder) {
        super(context);
        this.builder = builder;
        this.orientation = builder.getOrientation();
        this.listener = builder.getListener();
        this.callback = builder.getCallback();
        initView(context, builder.getMenuRes());
    }

    /**
     * Called to build the View layout
     *
     * @param context The current app {@code Context}
     * @param menuRes The menu res id
     */
    private void initView(Context context, int menuRes) {
        weakContext = new WeakReference<>(context);
        rootLinearLayout = (LinearLayout) LayoutInflater.from(context)
                .inflate((orientation == VERTICAL ? R.layout.menu_linearlayout_vertical :
                                R.layout.menu_linearlayout_horizontal)
                        , null,
                        true);

        setContentView(rootLinearLayout);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(null);
        setAnimationStyle(0);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        setFocusable(true);
        setTouchable(true);

        setMenu(menuRes);
    }

    /**
     * Called when you want to show the FAB menu.
     *
     * @param anchor The {@code View} that the FAB menu is anchored to.
     */
    public void show(@NonNull final View anchor) {
        this.currentAnchor = anchor;

        showAtLocation(anchor,
                Gravity.BOTTOM | Gravity.END,
                getXPoint(),
                getYPoint() + getNavigationBarSize().y);

        getContentView().getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animateWindowInCircular(anchor, getContentView());
            }
        });

        int offset=1;
        for (View v : childrenViews) { animateButtonIn(v, ANIMATION_DURATION / offset++); }
    }

    /** @return The custom X point if provided in the {@link #builder} or a default point */
    private int getXPoint() {
        if (builder.getXOffset()!=-1) { return builder.getXOffset(); }
        return dipToPixels((orientation==VERTICAL ?
                R.dimen.fab_menu_end_vertical_margin :
                R.dimen.fab_menu_bottom_horizontal_margin));
    }

    /** @return The custom Y point if provided in the {@link #builder} or a default point */
    private int getYPoint() {
        if (builder.getYOffset()!=-1) { return builder.getYOffset(); }
        return dipToPixels((orientation==VERTICAL ?
                        R.dimen.fab_menu_bottom_vertical_margin :
                        R.dimen.fab_menu_end_horizontal_margin));
    }

    /**
     * This is used to determine the height of the Navigation Bar that comes on devices like
     * Nexus and Pixel. Samsung had a hardware up until the S8 which messed with the position of
     * the popup window.
     *
     * @return A {@code Point} with the height of the Navigation Bar as the Y point.
     */
    private Point getNavigationBarSize() {
        Point appUsableSize = getAppUsableScreenSize();
        Point realScreenSize = getRealScreenSize();

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    /** @return The screen minus the navigation bar */
    private Point getAppUsableScreenSize() {
        WindowManager windowManager =
                (WindowManager) weakContext.get().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /** @return The entire screen size. */
    private Point getRealScreenSize() {
        WindowManager windowManager =
                (WindowManager) weakContext.get().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    @Override
    public void dismiss() { animateWindowOutCircular(currentAnchor, getContentView()); }

    /**
     * Called to inflate the menu xml with the {@link MenuBuilder}
     *
     * @param menuRes The menu xml file id.
     */
    private void setMenu(@MenuRes int menuRes) {
        //noinspection RestrictedApi
        Menu menu = new MenuBuilder(weakContext.get());
        new MenuInflater(weakContext.get()).inflate(menuRes, menu);
        setUpMenu(menu);
    }

    /**
     * Called to loop through the {@code Menu} object and build each menu item for the FAB menu.
     *
     * @param menu The {@code Menu} object that contains the item for the FAB menu.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMenu(@NonNull Menu menu) {
        if (menu.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(weakContext.get());
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                ViewGroup viewGroup = (ViewGroup) inflater.inflate(
                        (orientation==VERTICAL ? R.layout.menu_list_item_vertical_1 :
                                R.layout.menu_list_item_horizontal_1),
                        rootLinearLayout,
                        false);

                viewGroup.setOnClickListener(onClickListener);
                viewGroup.setOnLongClickListener(onLongClickListener);
                viewGroup.setTag(R.id.tag_menu_item_id, item.getItemId());

                ((TextView)viewGroup.findViewById(R.id.txt_item_title))
                        .setText(item.getTitle().toString());
                CircleColorImageView fab = (CircleColorImageView) viewGroup.findViewById(R.id.fab);
                if (callback!=null) {
                    SparseIntArray colors = callback.getMenuItemColors();
                    if (colors!=null && colors.size()>0) {
                        fab.setBackground(getIconDrawable(R.drawable.circle_tintable_mini,
                                colors.get(item.getItemId(), android.R.color.white), true));
                    }
                }
                fab.setImageDrawable(item.getIcon());

                rootLinearLayout.addView(viewGroup);
                childrenViews.add(viewGroup);
            }
        }
    }

    /** @return {@code true} is the menu is showing, {@code false} if it is not. */
    public boolean isMenuOpen() { return isMenuOpen; }

    /**
     * The {@code View.OnClickListener} set to the FAB menu items. Calls the
     * {@link OnFABMenuItemClickListener} if not null
     */
    private View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onItemClicked((int) v.getTag(R.id.tag_menu_item_id));
                    }
                }
            };

    /**
     * The {@code View.OnLongClickListener} set to the FAB menu items. Calls the
     * {@link OnFABMenuItemClickListener} if not null
     */
    private View.OnLongClickListener onLongClickListener =
            new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener != null &&
                            listener.onItemLongClicked((int) v.getTag(R.id.tag_menu_item_id));
                }
            };

    /**
     * Animates the entire FAB menu layout into view, using the reveal animation.
     *
     * @param anchor      The view the FAB menu is anchored to.
     * @param contentView The root view of this {@link PopupWindow}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowInCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(contentView,
                coordinates.first,
                coordinates.second,
                0,
                Math.max(contentView.getWidth(), contentView.getHeight()));
        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                if (callback!=null) { callback.startFABIconAnimation(true); }
            }

            @Override public void onAnimationEnd(Animator animation) { isMenuOpen ^= true; }

            @Override public void onAnimationCancel(Animator animation) { }

            @Override public void onAnimationRepeat(Animator animation) { }
        });
        animator.start();
    }

    /**
     * Animates the entire FAB menu layout out of view, using the reveal animation.
     *
     * @param anchor      The view the FAB menu is anchored to.
     * @param contentView The root view of this {@link PopupWindow}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowOutCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(getContentView(),
                coordinates.first,
                coordinates.second,
                Math.max(getContentView().getWidth(), getContentView().getHeight()),
                0);

        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                if (callback!=null) { callback.startFABIconAnimation(false); }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isMenuOpen ^= true;
                FABMenu.super.dismiss();
            }

            @Override public void onAnimationCancel(Animator animation) { }

            @Override public void onAnimationRepeat(Animator animation) { }
        });

        animator.start();
    }

    /**
     * This will animate each of the FAB menu item into view.
     *
     * @param button The {@code View} of the menu item.
     * @param delay  The delay offset so that the menu item animation does not happen while the
     *               overall reveal animation has not completed.
     */
    private void animateButtonIn(View button, int delay) {
        AnimationSet animation = new AnimationSet(true);
        Animation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

        animation.addAnimation(scale);
        animation.setInterpolator(new OvershootInterpolator(1));
        animation.setDuration(ANIMATION_DURATION);
        animation.setStartOffset(delay);
        button.startAnimation(animation);
    }

    /**
     * Used to calculate the position where the user clicked the FAB.
     *
     * @param anchor      The view the FAB menu is anchored to.
     * @param contentView The root view of this {@link PopupWindow}
     * @return A {@code Pair} object that contains the x and y of the click.
     */
    private Pair<Integer, Integer> getClickOrigin(@Nullable View anchor,
                                                  @NonNull View contentView) {
        if (anchor == null) return new Pair<>(0, 0);

        final int[] anchorCoordinates = new int[2];
        anchor.getLocationOnScreen(anchorCoordinates);
        anchorCoordinates[0] += anchor.getWidth() / 2;
        anchorCoordinates[1] += anchor.getHeight() / 2;

        final int[] contentCoordinates = new int[2];
        contentView.getLocationOnScreen(contentCoordinates);

        int x = anchorCoordinates[0] - contentCoordinates[0];
        int y = anchorCoordinates[1] - contentCoordinates[1];

        return new Pair<>(x, y);
    }

    /**
     * Called to convert a {@code float} dip into pixels.
     *
     * @param dimenId The {@code DimenRes} id of the dimen to convert into pixels.
     * @return The converted dip into pixels.
     */
    private int dipToPixels(@DimenRes int dimenId) {
        return weakContext.get().getResources().getDimensionPixelSize(dimenId);
    }

    /**
     * Get the color from a color resource.
     *
     * @param id      The {@code ColorRes} id that you want
     * @return The color as an int.
     */
    private int getColor(@ColorRes int id) { return weakContext.get().getColor(id); }

    /**
     * This can be used to determine if the color given is not dark enough for a white foreground
     * color.
     *
     * @param colorIntValue The color to see if is too dark or light.
     * @return White or black based off of the given color.
     */
    private int getContrastColor(int colorIntValue) {
        int red = Color.red(colorIntValue);
        int green = Color.green(colorIntValue);
        int blue = Color.blue(colorIntValue);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }

    /**
     * Called to get a {@link Drawable}
     *
     * @param drawableId The drawable id. {@link DrawableRes}
     * @param colorId    The {@code int} {@link ColorRes} color id to tint the drawable, or
     *                   {@code -2} to not tint the drawable.
     * @param isMutate   {@code true} if the drawable should be mutate. {@link Drawable#mutate()}
     * @return The {@code Drawable} object.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    private Drawable getIconDrawable(@DrawableRes int drawableId, int colorId, boolean isMutate) {
        Drawable drawable = weakContext.get().getDrawable(drawableId);

        if (drawable==null) { return null; }

        drawable = isMutate ?
                DrawableCompat.wrap(drawable).mutate() : DrawableCompat.wrap(drawable);
        if (colorId>-2) { changeDrawableTint(drawable, colorId); }
        return drawable;
    }

    /**
     * Called to tint a {@link Drawable}
     *
     * @param drawable The {@code Drawable} to tint the color.
     * @param colorId  The {@link ColorRes} id for the color to tint to.
     */
    private void changeDrawableTint(@NonNull Drawable drawable, @ColorRes int colorId) {
        DrawableCompat.setTint(drawable, getColor(colorId));
    }
}
