package com.felipecsl.elifut.activitiy;

import retrofit.Response;
import rx.Observer;

public abstract class SimpleResponseObserver<T> implements Observer<Response<T>> {
  @Override public void onCompleted() {
  }
}
