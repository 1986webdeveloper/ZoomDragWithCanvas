package com.example.acquaint.canvasexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    DemoView demoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = findViewById(R.id.rl_layout);
        demoview = new DemoView(this, relativeLayout);
//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(20, 20, 20, 20);
        relativeLayout.addView(demoview, lp);
//        DemoViewNew demoViewNew = new DemoViewNew(this);
//        relativeLayout.addView(demoViewNew);

    }
}
