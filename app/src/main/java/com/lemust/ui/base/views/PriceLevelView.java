package com.lemust.ui.base.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lemust.R;


public class PriceLevelView extends LinearLayout {
    private View rootView;
    private ImageView[] stars = new ImageView[4];


    public PriceLevelView(Context context) {
        super(context);
        init(context);
    }

    public PriceLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_price_level, this);
        this.stars[0] = rootView.findViewById(R.id.iv_price_level1);
        this.stars[1] = rootView.findViewById(R.id.iv_price_level2);
        this.stars[2] = rootView.findViewById(R.id.iv_price_level3);
        this.stars[3] = rootView.findViewById(R.id.iv_price_level4);
    }

    public void setRating(int rating) {
        if (rating > 4) rating = 4;
        for (int i = 0; i < rating; i++) {
            stars[i].setImageResource(R.drawable.ic_icn_place_price_active_white );
        }


    }


}