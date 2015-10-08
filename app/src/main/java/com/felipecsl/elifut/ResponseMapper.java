package com.felipecsl.elifut;

import retrofit.Response;
import rx.Observable;
import rx.functions.Func1;

/** Maps a {@link Response} into an error {@link Observable} if it's unsuccessful. */
public final class ResponseMapper<T> implements Func1<Response<T>, Observable<Response<T>>> {
  private static final ResponseMapper INSTANCE = new ResponseMapper();

  private ResponseMapper() {
  }

  public static <T> ResponseMapper<T> instance() {
    //noinspection unchecked
    return INSTANCE;
  }

  @Override
  public Observable<Response<T>> call(Response<T> response) {
    if (response.isSuccess()) {
      return Observable.just(response);
    }
    return Observable.error(new Throwable());
  }
}
