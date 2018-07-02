package com.example.chenzexuan.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.scalpel.ScalpelFrameLayout;

public class ViewDebugHelper {

  public static final String DEBUG_VIEW_ON = "3D ON";
  public static final String DEBUG_VIEW_OFF = "3D OFF";
  public static final String DEBUG_ALL = "ALL ";
  public static final String DEBUG_IDS = "IDS ";
  public static final String DEBUG_VIEWS = "VIEWS ";

  public boolean viewDebug = false;
  private boolean viewIdsDebug = false;
  private boolean viewViewsDebug = false;
  private ScalpelFrameLayout scalpelFrameLayout;

  public static ViewDebugHelper sINSTANCE = new ViewDebugHelper();

  public static ViewDebugHelper getInstance() {
    return sINSTANCE;
  }

  public void setContentView(Activity activity, int layoutResID) {
    scalpelFrameLayout = new ScalpelFrameLayout(activity);
    View mainView = activity.getLayoutInflater().inflate(layoutResID, null);
    scalpelFrameLayout.addView(mainView);
    renderScalpel(scalpelFrameLayout);
    activity.setContentView(scalpelFrameLayout);
    renderDebugView(activity, scalpelFrameLayout);
  }

  private void renderScalpel(ScalpelFrameLayout scalpelFrameLayout) {
    scalpelFrameLayout.setLayerInteractionEnabled(viewDebug);
    scalpelFrameLayout.setDrawIds(viewIdsDebug);
    scalpelFrameLayout.setDrawViews(viewViewsDebug);
  }

  private void renderDebugView(Activity activity, ScalpelFrameLayout scalpelFrameLayout) {
    LinearLayout debugViewLayout = new LinearLayout(activity);
    debugViewLayout.setBackgroundColor(Color.parseColor("#22000000"));
    debugViewLayout.setOrientation(LinearLayout.HORIZONTAL);

    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100, 1);
    lp.gravity = Gravity.CENTER;

    TextView debugSwitch = new TextView(activity);
    debugSwitch.setGravity(Gravity.CENTER);
    debugSwitch.setText(String.format("%s%s", DEBUG_ALL, DEBUG_VIEW_OFF));
    debugSwitch.setOnClickListener(
        v -> {
          viewDebug = !viewDebug;
          debugSwitch.setText(String.format("%s%s", DEBUG_ALL, viewDebug ? DEBUG_VIEW_ON : DEBUG_VIEW_OFF));
          renderScalpel(scalpelFrameLayout);
        });
    debugViewLayout.addView(debugSwitch, lp);

    TextView debugIdSwitch = new TextView(activity);
    debugIdSwitch.setGravity(Gravity.CENTER);
    debugIdSwitch.setText(String.format("%s%s", DEBUG_IDS, DEBUG_VIEW_OFF));
    debugIdSwitch.setOnClickListener(
        v -> {
          viewIdsDebug = !viewIdsDebug;
          debugIdSwitch.setText(String.format("%s%s", DEBUG_IDS, viewIdsDebug ? DEBUG_VIEW_ON : DEBUG_VIEW_OFF));
          renderScalpel(scalpelFrameLayout);
        });
    debugViewLayout.addView(debugIdSwitch, lp);

    TextView debugViewsSwitch = new TextView(activity);
    debugViewsSwitch.setGravity(Gravity.CENTER);
    debugViewsSwitch.setText(String.format("%s%s", DEBUG_VIEWS, DEBUG_VIEW_OFF));
    debugViewsSwitch.setOnClickListener(
        v -> {
          viewViewsDebug = !viewViewsDebug;
          debugViewsSwitch.setText(String.format("%s%s", DEBUG_VIEWS, viewViewsDebug ? DEBUG_VIEW_ON : DEBUG_VIEW_OFF));
          renderScalpel(scalpelFrameLayout);
        });
    debugViewLayout.addView(debugViewsSwitch, lp);

    FrameLayout.LayoutParams flp =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 100);
    flp.gravity = Gravity.BOTTOM;
    activity.addContentView(debugViewLayout, flp);
  }
}
