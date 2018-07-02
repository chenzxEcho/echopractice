package com.example.chenzexuan.myapplication.nohook;

import android.util.Log;

public class LogUtil {
  public static final String LOG_NAME = "czxtest";

  public static void e(String content) {
    Log.e(LOG_NAME, content);
  }
}
