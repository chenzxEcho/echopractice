package com.example.chenzexuan.myapplication;

import android.widget.Toast;


public class U {

  public static void toast(String content) {
    Toast.makeText(DefaultApplication.getContext(), content, Toast.LENGTH_SHORT).show();
  }
}
