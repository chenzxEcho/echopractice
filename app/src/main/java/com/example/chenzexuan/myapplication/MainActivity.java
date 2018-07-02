package com.example.chenzexuan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.example.chenzexuan.myapplication.listview.ListTest;
import com.example.chenzexuan.myapplication.nohook.HookUtil;
import com.example.chenzexuan.myapplication.nohook.LogUtil;

import hugo.weaving.DebugLog;

public class MainActivity extends BaseActivity {


  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    if (requestCode == 10001) {
    //      LogUtil.e(resultCode + "");
    //      TextView tv = findViewById(R.id.add_item);
    //      tv.setText(data.getStringExtra("data"));
    //    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    ListTest.start(this);
//    ActivityOptionsTest.start(this);
//    NotificationTest.start(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
//    NotificationTest.onResume(this);
    RxjavaTest.start();
  }
}
