package com.felipecsl.elifut.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

public final class CollectionUtils {
  private CollectionUtils() {
  }

  public static <T> List<T> shuffle(List<T> list) {
    Collections.shuffle(list);
    return list;
  }

  public static <T> List<T> sort(List<T> list, Comparator<? super T> comparator) {
    Collections.sort(list, comparator);
    return list;
  }

  public static <T> List<T> toList(Observable<T> observable) {
    return observable.toList().toBlocking().first();
  }
}
