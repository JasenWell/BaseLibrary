package com.git.jasenwell.baselibproject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Launcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("HJH_LAUNCHER_TEST");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        textView.setTextColor(Color.BLUE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Launcher.this,"onClick Test Button",Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(textView);
    }

    private void test(){
        MainProduct product = new MainProduct.Builder(getApplicationContext(),R.style.dialog)
                .setDes("des")
                .setName("name")
                .setId(1)
                .create();
    }
}
