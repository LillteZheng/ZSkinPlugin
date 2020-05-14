package com.zhengsr.zskinplugin;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.skinlib.ZSkin;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+
            File.separator+"skinres-release.apk";
    String pkgName = "com.zhengsr.skinres";
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ZSkin.setFactory(this)
                .loadSkinNow(path)
                .load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void changeSkin(View view) {
        ZSkin.changeSkin(path);
       // SkinFactory.loadSkin(path,pkgName);
    }
}
