package com.example.chenzexuan.myapplication.nohook;

import android.content.Context;

import com.example.chenzexuan.myapplication.ScanClassHelper;
import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class HookUtil {
  private static final String[] whiteList = new String[] {"chenzexuan"};

  // hook操作内的方法请加入黑名单
  private static final String[] blackList = new String[] {"nohook", "RxjavaTest"};

  private static final int ANNOTATION = 1;
  private static final int ALL = 2;       // 不建议
  private static final int HOOK_TYPE = ANNOTATION;

  private static XC_MethodHook hook =
      new XC_MethodHook() {
        long hookId;

        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
          super.beforeHookedMethod(param);
          hookId = System.currentTimeMillis();
          if (param.thisObject != null
              && param.thisObject.getClass() != null
              && param.method != null) {
            LogUtil.e(
                "HOOK START  ||| class -> "
                    + param.thisObject.getClass().getSimpleName()
                    + " method -> "
                    + param.method.getName()
                    + " ||| params -> "
                    + Arrays.toString(param.args));
          }
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
          super.afterHookedMethod(param);
          LogUtil.e(
              "HOOK END!!  ||| result = "
                  + (param.getResult() == null ? " void method" : param.getResult().toString()));
        }
      };

  public static void startHook(Context context) {
    int hookCount = 0;
    long startTime = System.currentTimeMillis();
    ArrayList<String> list = ScanClassHelper.getClassName(context);
    for (String className : list) {
      try {
        Class clazz = Class.forName(className);
        if (!isAllow(clazz.getName())) {
          continue;
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
          if (hookMethod(clazz, method)) {
            hookCount++;
          }
        }
      } catch (ClassNotFoundException e) {
        LogUtil.e(e.toString());
      }
    }
    LogUtil.e(
        "hook count = " + hookCount + " total time = " + (System.currentTimeMillis() - startTime));
  }

  private static boolean hookMethod(Class clazz, Method method) {
    switch (HOOK_TYPE) {
      case ANNOTATION:
        return hookByAnnotation(clazz, method);
      case ALL:
        return hookAll(clazz, method);
      default:
        return false;
    }
  }

  private static boolean hookAll(Class clazz, Method method) {
    try {
      DexposedBridge.hookAllMethods(clazz, method.getName(), hook);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean hookByAnnotation(Class clazz, Method method) {
    try {
      Annotation[] annotations = method.getDeclaredAnnotations();
      boolean needHook = false;
      for (Annotation annotation : annotations) {
        if (annotation instanceof HookLog) {
          needHook = true;
        }
      }
      if (needHook) {
        DexposedBridge.hookAllMethods(clazz, method.getName(), hook);
      }
      return needHook;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean isAllow(String clazzName) {
    boolean result = true;
    for (String whiteName : Arrays.asList(whiteList)) {
      for (String blackName : Arrays.asList(blackList)) {
        if (!clazzName.contains(whiteName) || clazzName.contains(blackName)) {
          result = false;
        }
      }
    }
    return result;
  }
}
