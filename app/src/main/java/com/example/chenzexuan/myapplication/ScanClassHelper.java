package com.example.chenzexuan.myapplication;

import android.app.Activity;
import android.content.Context;

import com.example.chenzexuan.myapplication.nohook.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class ScanClassHelper {
  private static String packageName = "com.example.chenzexuan.myapplication.";

  public static ArrayList<String> getClassName(Context context) {
    String[] tempPath = context.getPackageCodePath().split("/");
    String apkPath =
        context
            .getPackageCodePath()
            .substring(
                0, context.getPackageCodePath().length() - tempPath[tempPath.length - 1].length());
    ArrayList<DexFile> dexFiles = new ArrayList<>();
    File[] fileArray = new File(apkPath).listFiles();
    for (int i = 0; i < fileArray.length; i++) {
      try {
        DexFile dexFile = new DexFile(fileArray[i]);
        dexFiles.add(dexFile);
      } catch (Exception e) {
        LogUtil.e("dexFile error: index is " + i + ", name is " + fileArray[i].getName());
      }
    }
    ArrayList<String> classNameList = new ArrayList<String>();
    for (DexFile dexFile : dexFiles) {
      try {
        Enumeration<String> enumeration =
            dexFile.entries(); // 获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
        while (enumeration.hasMoreElements()) { // 遍历
          String className = (String) enumeration.nextElement();
          if (className.contains(packageName)) { // 在当前所有可执行的类里面查找包含有该包名的所有类
            classNameList.add(className);
          }
        }
      } catch (Exception e) {
        LogUtil.e("class find error");
      }
    }
    return classNameList;
  }
}
