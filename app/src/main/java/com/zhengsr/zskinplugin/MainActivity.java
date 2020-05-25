package com.zhengsr.zskinplugin;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengsr.skinlib.ZSkin;
import com.zhengsr.skinlib.utils.ZUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
