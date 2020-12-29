package com.git.jasenwell.baselibproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.hjh.baselib.base.LBaseActivity;
import com.hjh.baselib.file.FileScaner;
import com.hjh.baselib.file.ImageFileDirectory;

import java.util.List;

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
        textView.setText("Hello World! baselib test2");
        checkCameraPermission();
    }

    @Override
    public boolean showPermissionsResult(boolean result) {
        return false;
    }

    @Override
    public void onResponsePermissionsResult(boolean result) {
        if(result){
            List<ImageFileDirectory>  list = FileScaner.getInstance().scanImageFile(this,true);
        }
    }
}
