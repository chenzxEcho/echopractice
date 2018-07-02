package com.example.chenzexuan.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.chenzexuan.myapplication.MainActivity;
import com.example.chenzexuan.myapplication.nohook.HookUtil;

/**
 * 这个类只是为了方便获取全局Context的.
 *
 * @author weishu
 * @date 16/3/29
 */
public class DefaultApplication extends Application {

    private static Context sContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;
        HookUtil.startHook(this);
    }

    public static Context getContext() {
        return sContext;
    }
}
