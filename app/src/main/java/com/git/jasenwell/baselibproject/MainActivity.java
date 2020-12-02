package com.git.jasenwell.baselibproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.hjh.baselib.base.LBaseActivity;

import butterknife.BindView;

public class MainActivity extends LBaseActivity {

    @BindView(R.id.tv_test)
    TextView textView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onLoadDefaultData() {
        super.onLoadDefaultData();
        textView.setText("Hello World! baselib");
    }
}
