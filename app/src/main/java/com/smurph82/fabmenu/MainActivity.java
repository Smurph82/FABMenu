package com.smurph82.fabmenu;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.View;

import com.smurph82.fabextender.menu.FABMenu;
import com.smurph82.fabextender.menu.FABMenuBuilder;
import com.smurph82.fabextender.menu.SimpleFABMenuCustomCallback;
import com.smurph82.fabextender.menu.SimpleOnFABMenuItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private FABMenu fabMenu;

    @BindView(R.id.fab) FloatingActionButton fab;

    private AnimatedVectorDrawable avdSubjectToClose;
    private AnimatedVectorDrawable avdCloseToSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ButterKnife.bind(this);

        avdSubjectToClose = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_subject_to_close);
        avdCloseToSubject = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_close_to_subject);
    }

    @OnClick({R.id.fab})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.fab: openMenu(); break;
            default: Timber.d("OnClick: Unhandled view clicked.");
        }
    }

    /**
     *
     */
    private void openMenu() {
        if (fabMenu == null) {
            FABMenuBuilder builder = new FABMenuBuilder()
                    .setMenuRes(R.menu.menu_fab)
                    .setOrientation(FABMenu.VERTICAL)
                    .setListener(listener)
                    .setCallback(callback);
//                    .setYOffset(dipToPixels(R.dimen.fab_offset_y_vertical));
            fabMenu = new FABMenu(this, builder);
        }
        fabMenu.show(fab);
    }

    /**
     * Called to convert a {@code float} dip into pixels.
     *
     * @param dimenId The {@code DimenRes} id of the dimen to convert into pixels.
     * @return The converted dip into pixels.
     */
    private int dipToPixels(@DimenRes int dimenId) {
        return getResources().getDimensionPixelSize(dimenId);
    }

    /**
     *
     */
    private SimpleOnFABMenuItemClickListener listener =
            new SimpleOnFABMenuItemClickListener() {
                @Override
                public void onItemClicked(@IdRes int id) {
                    switch (id) {
                        case R.id.action_save:
                            Timber.i("onItemClicked: Save");
                            break;
                        case R.id.action_delete:
                            Timber.i("onItemClicked: Delete");
                            break;
                        case R.id.action_add:
                            Timber.i("onItemClicked: Add");
                            break;
                        default: Timber.e("onItemClicked: Unhandled FABMenu item clicked");
                    }
                    fabMenu.dismiss();
                }
            };


    private static final SparseIntArray colors;
    static {
        colors = new SparseIntArray(3);
        colors.put(R.id.action_add, R.color.action_add);
        colors.put(R.id.action_delete, R.color.action_delete);
        colors.put(R.id.action_save, R.color.action_save);
    }
    private SimpleFABMenuCustomCallback callback =
            new SimpleFABMenuCustomCallback() {
                @Override public SparseIntArray getMenuItemColors() { return colors; }

                @Override public void startFABIconAnimation(boolean isOpening) {
                    if (isOpening) {
                        fab.setImageDrawable(avdSubjectToClose);
                        avdSubjectToClose.start();
                    } else {
                        fab.setImageDrawable(avdCloseToSubject);
                        avdCloseToSubject.start();
                    }
                }
            };
}
