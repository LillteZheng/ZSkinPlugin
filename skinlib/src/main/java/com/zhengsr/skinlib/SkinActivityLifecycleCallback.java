package com.zhengsr.skinlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zhengsr.skinlib.utils.LggUtils;

/**
 * @author by  zhengshaorui on 2019/9/6
 * Describe:
 */
class SkinActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ZSkin.inject(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
