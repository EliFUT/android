package com.elifut;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observer;

public abstract class CompletionObserver<T> implements Observer<T> {
  private final Context context;
  private final String logTag;
  private final String errorMessage;

  public CompletionObserver(Context context, String logTag, String errorMessage) {
    this.context = context;
    this.logTag = logTag;
    this.errorMessage = errorMessage;
  }

  @Override public void onNext(T t) {
  }

  @Override public void onError(Throwable e) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    Log.w(logTag, e);
  }
}
