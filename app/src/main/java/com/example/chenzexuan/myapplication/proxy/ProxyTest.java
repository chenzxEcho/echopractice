package com.example.chenzexuan.myapplication.proxy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.chenzexuan.myapplication.nohook.LogUtil;
import com.example.chenzexuan.myapplication.proxy.ams_hook.AMSHookHelper;
import com.example.chenzexuan.myapplication.proxy.classloder_hook.BaseDexClassLoaderHookHelper;
import com.example.chenzexuan.myapplication.proxy.classloder_hook.LoadedApkClassLoaderHookHelper;

import java.io.File;


public class ProxyTest {

  private static final int PATCH_BASE_CLASS_LOADER = 1;

  private static final int CUSTOM_CLASS_LOADER = 2;   // 没试通

  private static final int HOOK_METHOD = PATCH_BASE_CLASS_LOADER;

  public static void attachBaseContext(Context newBase) {
    try {
      Utils.extractAssets(newBase, "app-debug.apk");

      if (HOOK_METHOD == PATCH_BASE_CLASS_LOADER) {
        File dexFile = newBase.getFileStreamPath("app-debug.apk");
        File optDexFile = newBase.getFileStreamPath("app-debug.dex");
        BaseDexClassLoaderHookHelper.patchClassLoader(newBase.getClassLoader(), dexFile, optDexFile);
      } else {
        LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(newBase.getFileStreamPath("app-debug.apk"));
      }

      AMSHookHelper.hookActivityManagerNative();
      AMSHookHelper.hookActivityThreadHandler();

    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void onCreate(Activity activity) {
    LogUtil.e("我被调用了");
    Button t = new Button(activity);
    t.setText("test button");

    activity.setContentView(t);

    LogUtil.e("context classloader: " + activity.getApplicationContext().getClassLoader());
    t.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          Intent t = new Intent();
          if (HOOK_METHOD == PATCH_BASE_CLASS_LOADER) {
            t.setComponent(new ComponentName("com.example.chenzexuan.pluginapp",
                "com.example.chenzexuan.pluginapp.PluginActivity"));
          } else {
            t.setComponent(new ComponentName("com.example.chenzexuan.pluginapp",
                "com.example.chenzexuan.pluginapp.PluginActivity"));
          }
          activity.startActivity(t);
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    });
  }
}
