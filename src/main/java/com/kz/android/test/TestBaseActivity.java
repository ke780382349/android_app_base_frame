package com.kz.android.test;

import android.os.Bundle;
import android.widget.TextView;

import com.kz.android.base.KBaseActivity;
import com.kz.core.R;

/**
 * Created by Kezhuang on 2016/5/27.
 */
public class TestBaseActivity extends KBaseActivity {

    private TextView helloWorld;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_world);
        helloWorld = (TextView) findViewById(R.id.test_hello);
        helloWorld.setText("测试注解");
    }
}
