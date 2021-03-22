package com.lemust.ui.base.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lemust.R;


public class CircleIndicatorView extends LinearLayout {
    private View rootView;
    private View[] stars = new View[7];


    public CircleIndicatorView(Context context) {
        super(context);
        init(context);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_indicator, this);
        this.stars[0] = rootView.findViewById(R.id.iv_star1);
        this.stars[1] = rootView.findViewById(R.id.iv_star2);
        this.stars[2] = rootView.findViewById(R.id.iv_star3);
        this.stars[3] = rootView.findViewById(R.id.iv_star4);
        this.stars[4] = rootView.findViewById(R.id.iv_star5);
        this.stars[5] = rootView.findViewById(R.id.iv_star6);
        this.stars[6] = rootView.findViewById(R.id.iv_star7);
    }

    public void setCurrentSelectedItem(int position) {
        if (position <= 6) {
            for (int i = 0; i < 7; i++) {
                stars[i].setBackgroundResource(R.drawable.background_pager_indicator_un_selected_dot);
            }
            stars[position].setBackgroundResource(R.drawable.background_pager_indicator_selected_dot);

        }


    }


}