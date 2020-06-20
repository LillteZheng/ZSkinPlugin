package com.zhengsr.zskinplugin;

import android.Manifest;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhengsr.skinlib.ZSkin;
import com.zhengsr.skinlib.utils.ZUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    public void changeSkin(View view) {

        String path = getFilesDir().getAbsolutePath();
        String name = "skin_res-release.skin";
        String assetName = "skin/skin_res-release.skin";
        File file = new File(path,name);
        if (!file.exists()) {
            ZUtils.copyAssetFileToStorage(this,assetName,path,name);
        }

        ZSkin.loadSkin(file.getAbsolutePath());
    }
}
