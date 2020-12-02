package com.hjh.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.hjh.baselib.R;


public class LoadingView extends RelativeLayout {
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view= LayoutInflater.from(context).inflate(R.layout.loading_my,null,false);

        addView(view);
    }
}
