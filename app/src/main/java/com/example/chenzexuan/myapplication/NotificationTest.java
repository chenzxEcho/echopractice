package com.example.chenzexuan.myapplication;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class NotificationTest {

  public static void start(Activity activity) {
    activity.setContentView(R.layout.activity_notification);
    TextView state = activity.findViewById(R.id.notification_state);
    state.setText(String.format("%s", isNotificationEnabled(activity)));
    activity
        .findViewById(R.id.plan_1)
        .setOnClickListener(
            v -> {
              // 三星测试通过 nexus6P通过测试 一加通过测试 华为通过测试8.0.0 通知页
              Intent intent = new Intent();
              intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
              intent.putExtra("android.provider.extra.APP_PACKAGE", activity.getPackageName());
              activity.startActivity(intent);
            });

    activity
        .findViewById(R.id.plan_2)
        .setOnClickListener(
            v -> {
              // vivo测试通过 oppo通过测试6.0.1  通知页
              Intent intent = new Intent();
              intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
              intent.putExtra("app_package", activity.getPackageName());
              intent.putExtra("app_uid", activity.getApplicationInfo().uid);
              activity.startActivity(intent);
            });

    activity
        .findViewById(R.id.plan_3)
        .setOnClickListener(
            v -> {
              // 小米6x 红米note5   应用详情页
              Intent intent = new Intent();
              intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
              intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
              activity.startActivity(intent);
            });

    activity
        .findViewById(R.id.plan_4)
        .setOnClickListener(
            v -> {
              Intent intent = new Intent(Settings.ACTION_SETTINGS);
              activity.startActivity(intent);
            });
    activity
        .findViewById(R.id.plan_5)
        .setOnClickListener(
            v -> {
              Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
              activity.startActivity(intent);
            });
    activity
        .findViewById(R.id.plan_6)
        .setOnClickListener(
            v -> {
              // 小米6x 红米note5   应用详情页  同3
              Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
              activity.startActivity(intent);
            });
  }

  private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";

  private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

  public static boolean isNotificationEnabled(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      NotificationManager notificationManager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      return notificationManager == null || notificationManager.areNotificationsEnabled();
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
      ApplicationInfo appInfo = context.getApplicationInfo();
      String pkg = context.getApplicationContext().getPackageName();
      int uid = appInfo.uid;
      try {
        Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
        Method checkOpNoThrowMethod =
            appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
        Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
        int value = (int) opPostNotificationValue.get(Integer.class);
        return ((int) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg)
            == AppOpsManager.MODE_ALLOWED);
      } catch (ClassNotFoundException
          | NoSuchMethodException
          | NoSuchFieldException
          | InvocationTargetException
          | IllegalAccessException
          | RuntimeException e) {
        return true;
      }
    } else {
      return true;
    }
  }

  public static void onResume(Activity activity) {
    TextView state = activity.findViewById(R.id.notification_state);
    state.setText(String.format("%s", isNotificationEnabled(activity)));
  }
}
