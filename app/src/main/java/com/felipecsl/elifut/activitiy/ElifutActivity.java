package com.felipecsl.elifut.activitiy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.felipecsl.elifut.ElifutApplication;
import com.felipecsl.elifut.ElifutComponent;
import com.felipecsl.elifut.ResponseBodyMapper;
import com.felipecsl.elifut.ResponseMapper;
import com.felipecsl.elifut.services.ElifutService;

import javax.inject.Inject;

import icepick.Icepick;
import retrofit.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class ElifutActivity extends AppCompatActivity {

  @Inject ElifutService service;

  protected ElifutApplication getElifutApp() {
    return (ElifutApplication) getApplication();
  }

  protected ElifutComponent daggerComponent() {
    return getElifutApp().component();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  public <T> Observable.Transformer<Response<T>, T> applyTransformations() {
    return new Observable.Transformer<Response<T>, T>() {
      @Override public Observable<T> call(Observable<Response<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
            .flatMap(ResponseMapper.<T>instance())
            .map(ResponseBodyMapper.<T>instance())
            .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }
}
