package com.smurph82.fabextender.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.smurph82.fabextender.R;

/**
 * Created by Ben Murphy on 4/22/17.
 *
 * Used to be a simple ImageView that is in the shape of a Circle that can be colored.
 */

@SuppressWarnings("unused")
public class CircleColorImageView extends AppCompatImageView {

    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 2;

    private int size = SIZE_MINI;

    public CircleColorImageView(Context context) { this(context, null); }

    public CircleColorImageView(Context context, AttributeSet attrs) { this(context, attrs, 0); }

    public CircleColorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int circleColor = Color.WHITE;

        if (attrs != null) {
            TypedArray typedArray = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.CircleColorImageView, 0, 0);
            circleColor = typedArray.getColor(R.styleable.CircleColorImageView_circleColor,
                    Color.WHITE);
            size = typedArray.getInt(R.styleable.CircleColorImageView_circleSize, SIZE_MINI);
            typedArray.recycle();
        }

//        Drawable circle = context.getResources().getDrawable(R.drawable.circle_tintable);
        Drawable circle = context.getDrawable((size==SIZE_MINI ?
                R.drawable.circle_tintable_mini : R.drawable.circle_tintable_normal));
        circle.setColorFilter(circleColor, PorterDuff.Mode.SRC_IN);

        setBackgroundDrawable(circle);
    }

    /**
     * @return The set size of the CircleColorImageView
     * @see #SIZE_MINI
     * @see #SIZE_NORMAL
     */
    public int getSize() { return size; }
}
