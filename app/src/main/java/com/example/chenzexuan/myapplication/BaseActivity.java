package com.example.chenzexuan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

  @Override
  public void setContentView(int layoutResID) {
    ViewDebugHelper.getInstance().setContentView(this, layoutResID);
  }
}
