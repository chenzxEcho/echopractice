package com.example.chenzexuan.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

class ActivityOptionsTest {

  public static void start(Activity activity) {
    activity.setContentView(R.layout.activity_option_compat);
    activity
        .findViewById(R.id.next_page)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                //                activity.startActivity(new Intent(activity,
                // OptionsCompatActivity.class));
                activity.startActivity(
                    new Intent(activity, OptionsCompatActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity, v, "image")
                        .toBundle());
              }
            });
  }
}
