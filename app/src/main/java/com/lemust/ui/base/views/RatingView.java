package com.lemust.ui.base.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lemust.R;


public class RatingView extends LinearLayout {
    private View rootView;
    private ImageView[] stars = new ImageView[5];


    public RatingView(Context context) {
        super(context);
        init(context);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_custom_rating, this);
        this.stars[0] = rootView.findViewById(R.id.iv_rating1);
        this.stars[1] = rootView.findViewById(R.id.iv_rating2);
        this.stars[2] = rootView.findViewById(R.id.iv_rating3);
        this.stars[3] = rootView.findViewById(R.id.iv_rating4);
        this.stars[4] = rootView.findViewById(R.id.iv_rating5);
    }

    public void setRating(Float rating) {
        if (rating > 5) rating = 5f;
        double frac = rating % 1;
        int y = rating.intValue();
        for (int i = 0; i < y; i++) {
            stars[i].setImageResource(R.mipmap.icn_place_rating_star_full);
        }
        if (frac >= 0.5 && frac <= 0.99) {
            stars[y].setImageResource(R.mipmap.icn_place_rating_star_half);
        }
        Log.d("test", "test");

    }


}