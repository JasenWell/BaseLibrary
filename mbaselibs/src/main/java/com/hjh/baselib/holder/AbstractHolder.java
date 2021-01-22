package com.git.jasenwell.appcoat1.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbstractHolder {

    protected View rootView;
    protected Context context;

    public AbstractHolder(Context context){
        this.context = context;
        initView(context);
    }


    public void initView(Context context) {
        rootView = LayoutInflater.from(context).inflate(getLayoutId(),null);
        findViewById();
    }

    public View getRootView() {
        return rootView;
    }

    public void findViewById(View view,int id){
        view = rootView.findViewById(id);
    }

    public abstract void findViewById();
    public abstract int getLayoutId();
}
