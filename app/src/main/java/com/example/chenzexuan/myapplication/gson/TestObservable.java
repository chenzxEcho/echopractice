package com.example.chenzexuan.myapplication.gson;

import android.telecom.Call;

import com.example.chenzexuan.myapplication.nohook.LogUtil;
import com.example.chenzexuan.myapplication.JsonAdapter;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;

/**
 * a special observable that runs Tantan specific requests. 1. only one subscriber is allowed 2.
 * this observable must have one onNext/onComplete or one onError 3. perform transformations of
 * errors about global ui
 *
 * <p>it works likes a Single, but we use Observable interface anyway
 */
public class TestObservable<T> extends Observable<T> {

  public static final String TAG = "NetReqObs";

  public static boolean debug_ioException = false;

  public TestObservable(Func0<String> request, JsonAdapter<T> parser, boolean overrideRetry) {
    super(new OnSubs<T>(request, null, overrideRetry));
  }

  public TestObservable(Func0<String> request, JsonAdapter<T> parser) {
    this(request, parser, false);
  }

  static class OnSubs<T> implements OnSubscribe<T>, Subscription {

    private Subscriber<? super T> subscriber;

    public OnSubs(Func0<String> request, JsonAdapter<T> parser, boolean overrideRetry) {}

    public boolean canRetry() {
      return true;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
      this.subscriber = subscriber;
      subscriber.add(this);
      LogUtil.e("subscribe test call 1");
      executeRequest();
    }

    public void onError(Exception e) {
      LogUtil.e("subscribe test call 4");
      if (canRetry() && retryCount < 2) {
        retryCount += 1;
        executeRequest();
      } else {
        callOnError(e);
      }
    }

    //    @Override
    //    public void onFailure(Call call, IOException e) {
    //      onError(e);
    //    }

    int retryCount = 0;

    public void onResponse(Call call, String response) throws IOException {
      if (isUnsubscribed()) {
        return;
      }
      LogUtil.e("subscribe test call 3");
      callOnError(new Exception());

      if (subscriber != null) {
        T t = (T) new Object();
        LogUtil.e("subscribe test call 6");
        subscriber.onNext(t);
        if (subscriber != null) {
          LogUtil.e("subscribe test call 7");
          subscriber.onCompleted();
        }
      }
    }

    private void callOnError(Throwable e) {
      LogUtil.e("subscribe test call 5");
      if (subscriber != null) {
        subscriber.onError(e);
      }
    }

    private void executeRequest() {
      try {
        LogUtil.e("subscribe test call 2");
        onResponse(null, "123");
      } catch (Exception e) {
        onError(e);
      }
    }

    @Override
    public void unsubscribe() {
      subscriber = null;
    }

    @Override
    public boolean isUnsubscribed() {
      return subscriber == null;
    }
  }
}
