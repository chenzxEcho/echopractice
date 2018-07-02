package com.example.chenzexuan.myapplication.loadapk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.chenzexuan.myapplication.BaseActivity;
import com.example.chenzexuan.myapplication.R;
import com.example.chenzexuan.myapplication.nohook.LogUtil;

public class SecondActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    TextView tv = findViewById(R.id.add_item);
    tv.setText("second page");
    tv.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            startActivityForResult(new Intent(SecondActivity.this, ThirdActivity.class), 10000);
            finish();
          }
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 10000) {
      LogUtil.e(resultCode + "");
      setResult(10001, data);
      finish();
      overridePendingTransition(0,0);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onDestroy() {
    LogUtil.e("second destroy" + System.currentTimeMillis());
    super.onDestroy();
  }

  @Override
  public void onDetachedFromWindow() {
    LogUtil.e("second onDetachedFromWindow" + System.currentTimeMillis());
    super.onDetachedFromWindow();
  }

}
