package com.example.chenzexuan.myapplication;

import com.example.chenzexuan.myapplication.gson.GsonTest;
import com.example.chenzexuan.myapplication.nohook.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class RxjavaTest {
  public static void start() {
    createObs("String").subscribe(s -> LogUtil.e("result =" + s));

    createObs("{\"id\"= \"12345\"}")
        .compose(jsonToBean(TestBean.class))
        .subscribe(testBean -> LogUtil.e(testBean.getId()));

    Observable.create(
            (Observable.OnSubscribe<Response>)
                subscriber -> {
                  Request.Builder builder =
                      new Request.Builder().url("https://www.baidu.com").get();
                  Request request = builder.build();
                  Call call = new OkHttpClient().newCall(request);
                  Response response = null;
                  try {
                    response = call.execute();
                  } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                  }
                  subscriber.onNext(response);
                })
        .compose(networkSwitch())
        .compose(json2Bean(TestBean.class))
        .subscribe(
            new Observer<TestBean>() {
              @Override
              public void onCompleted() {
                LogUtil.e("oncompleted");
              }

              @Override
              public void onError(Throwable e) {
                LogUtil.e("onError");
              }

              @Override
              public void onNext(TestBean testBean) {
                LogUtil.e("receive a testbean");
              }
            });
  }

  private static Observable.Transformer<Response, Response> networkSwitch() {
    return responseObservable ->
        responseObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> Observable<T> createObs(T t) {
    return Observable.create(
        subscriber -> {
          try {
            subscriber.onNext(t);
          } catch (Exception e) {
            subscriber.onError(e);
          }
        });
  }

  public static <T> Observable<T> createBs(Observable<T> obs) {
    BehaviorSubject<T> behaviorSubject = BehaviorSubject.create();
    obs.subscribeOn(Schedulers.newThread()).subscribe(behaviorSubject);
    return behaviorSubject;
  }

  public static Observable<Long> createIntervalLongBs(int delay, int period, TimeUnit timeUnit) {
    return createBs(createIntervalObs(delay, period, timeUnit));
  }

  public static Observable<Long> createIntervalObs(int delay, int period, TimeUnit timeUnit) {
    return Observable.interval(delay, period, timeUnit)
        .flatMap((Func1<Long, Observable<Long>>) Observable::just);
  }

  public static Observable<Integer> mapMultiple(Observable<Integer> obs, int multiple) {
    return obs.map(integer -> integer * multiple);
  }

  public static <T> Observable.Transformer<String, T> jsonToBean(Class<T> clazz) {
    return tObservable -> tObservable.map(s -> GsonTest.gson().fromJson(s, clazz));
  }

  public static <T> Observable.Transformer<Response, T> json2Bean(Class<T> clazz) {
    return tObservable ->
        tObservable.map(
            response -> {
              if (response.isSuccessful() && response.body() != null) {
                return GsonTest.gson().fromJson(response.body().toString(), clazz);
              }
              return null;
            });
  }
}
