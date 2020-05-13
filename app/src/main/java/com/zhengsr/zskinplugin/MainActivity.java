package com.zhengsr.zskinplugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhengsr.skinlib.SkinFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinFactory.setFactory(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
