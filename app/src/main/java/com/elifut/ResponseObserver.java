package com.elifut;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observer;

public abstract class ResponseObserver<T> implements Observer<T> {
  private final Context context;
  private final String logTag;
  private final String errorMessage;

  public ResponseObserver(Context context, String logTag, String errorMessage) {
    this.context = context;
    this.logTag = logTag;
    this.errorMessage = errorMessage;
  }

  @Override public void onCompleted() {
  }

  @Override public void onError(Throwable e) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    Log.w(logTag, e);
  }
}
