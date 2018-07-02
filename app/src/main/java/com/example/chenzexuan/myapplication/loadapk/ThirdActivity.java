package com.example.chenzexuan.myapplication.loadapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.chenzexuan.myapplication.BaseActivity;
import com.example.chenzexuan.myapplication.R;
import com.example.chenzexuan.myapplication.nohook.LogUtil;

public class ThirdActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView tv = findViewById(R.id.add_item);
    tv.setText("Third page");
    tv.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("data", "I'm data");
            setResult(10000, intent);
            finish();
            overridePendingTransition(0, 0);
          }
        });
  }

  @Override
  protected void onDestroy() {
    LogUtil.e("third destroy" + System.currentTimeMillis());
    super.onDestroy();
  }

  @Override
  public void onDetachedFromWindow() {
    LogUtil.e("third onDetachedFromWindow" + System.currentTimeMillis());
    super.onDetachedFromWindow();
  }
}
