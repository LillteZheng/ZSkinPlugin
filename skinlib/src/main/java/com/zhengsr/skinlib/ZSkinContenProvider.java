package com.zhengsr.skinlib;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zhengsr.skinlib.utils.LggUtils;
import com.zhengsr.skinlib.utils.ZSprefUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe: 通过注册一个 contentprovider 来拿到content
 */
public class ZSkinContenProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context instanceof Application) {
            ZSprefUtils.init(context);
            SkinManager.get().config(context);
            ZSkin.inject(context);
            ((Application) context).registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallback());
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
