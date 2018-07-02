package com.example.chenzexuan.myapplication.gson;

import android.content.Context;

import com.example.chenzexuan.myapplication.nohook.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;

public class GsonTest {

  public static void start(Context context) {
    //    //生成较大的json
    //    List<JsonObject> list = new ArrayList<JsonObject>();
    //    for (int i = 0; i < 100; i++) {
    //      JsonObject obj = new JsonObject();
    //      obj.addProperty("id", i);
    //      obj.addProperty("name", String.valueOf(i));
    //      obj.addProperty("age", String.valueOf(i / 33));
    //      for (int x = 0; x < 10; x++) {
    //        StringBuilder address = new StringBuilder();
    //        for (int j = 0; j < 200; j++) {
    //          address.append(i);
    //        }
    //        obj.addProperty("address" + x, x + x + x + x + address.toString());
    //      }
    //      list.add(obj);
    //    }

    Gson gson = new GsonBuilder().create();
    //    String str = gson.toJson(list);
    String str = createStr(context);
    LogUtil.e("log size = " + str.length());

    // 1,gson解析
    long start1 = System.currentTimeMillis();
    BeanTest beanTest = gson.fromJson(str, BeanTest.class);
    LogUtil.e("Conversation size:" + beanTest.data.conversations.size());
    LogUtil.e("gson time elapse:" + (System.currentTimeMillis() - start1));

    /*
    *
        if (response.request().url().encodedPath().contentEquals("/v1/users/me/conversations")) {
          in = Putong.me.getAssets().open("test.txt");
          HookLog.e("czxtest","log size = " + in.available());
        }
        if (in != null) {
          long start1 = System.currentTimeMillis();
          try {
            parsed = parser.parse(in);
            in.close();
          } catch (Exception e2) {
            App.me.logError(e2);
            callOnError(e2);
          }
          if (response.request().url().encodedPath().contentEquals("/v1/users/me/conversations")) {

            HookLog.e("czxtest", "jackson time elapse:" + (System.currentTimeMillis() - start1));
          }
          if (Config.DEBUG)
    *
    *
    *
    * */


  }

  private static String createStr(Context context) {
    return readAssetsTxt(context, "test");
  }

  public static String readAssetsTxt(Context context, String fileName){
    try {
      //Return an AssetManager instance for your application's package
      InputStream is = context.getAssets().open(fileName+".txt");
      int size = is.available();
      // Read the entire asset into a local byte buffer.
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      // Convert the buffer into a string.
      String text = new String(buffer, "utf-8");
      // Finally stick the string into the text view.
      return text;
    } catch (IOException e) {
      // Should never happen!
//            throw new RuntimeException(e);
      e.printStackTrace();
    }
    return "读取错误，请检查文件名";
  }

  public static Gson gson() {
    return new GsonBuilder().create();
  }
}
