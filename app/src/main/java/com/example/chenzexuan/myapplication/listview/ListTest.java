package com.example.chenzexuan.myapplication.listview;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.chenzexuan.myapplication.R;
import com.example.chenzexuan.myapplication.U;
import com.example.chenzexuan.myapplication.nohook.LogUtil;
import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;

import java.util.ArrayList;
import java.util.Arrays;

import hugo.weaving.DebugLog;

public class ListTest {

  /** 万能适配器的方法 */
  public static void start(Activity activity) {
    activity.setContentView(R.layout.activity_main);
    RecyclerView recyclerView = activity.findViewById(R.id.recycler_view);
    String[] datas =
        new String[] {
          "213124124",
          "fasfwef",
          "rwefwrt34t34",
          "3r2rfbeuwb43",
          "fby3bryu2b",
          "dwbfubub3",
          "r3u32fb3ufb3u4",
          "fwefwe34t34t34",
          "fwefewfef34t4r3434r",
          "jnong934bf3u4ig"
        };
    ArrayList<String> data = new ArrayList<>(Arrays.asList(datas));
    activity
        .findViewById(R.id.add_item)
        .setOnClickListener(
            v -> {
              data.add("this is new one " + data.size());
              recyclerView.getAdapter().notifyItemInserted(data.size() - 1);
             hookRxjava();
            });
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(
        new CommonRecyclerAdapter<String>(activity, data, R.layout.recycler_view_item) {
          @Override
          public void convert(CommonRecyclerViewHolder holder, String item) {
            ((TextView) holder.itemView.findViewById(R.id.recycler_view_item_text)).setText(item);
          }
        });
  }

  private static void hookRxjava() {
    DexposedBridge.hookAllMethods(U.class, "toast", new XC_MethodHook() {
      @Override
      protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        LogUtil.e("rxjava hook");
      }

      @Override
      protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        LogUtil.e("after rxjava hook");
      }
    });
  }
}
