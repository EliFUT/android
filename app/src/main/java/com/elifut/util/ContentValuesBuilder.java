package com.elifut.util;

import android.content.ContentValues;

public class ContentValuesBuilder {
  private final ContentValues delegate = new ContentValues();

  private ContentValuesBuilder() {
  }

  public static ContentValuesBuilder create() {
    return new ContentValuesBuilder();
  }

  public ContentValuesBuilder put(String key, String value) {
    delegate.put(key, value);
    return this;
  }

  public ContentValuesBuilder put(String key, Integer value) {
    delegate.put(key, value);
    return this;
  }

  public ContentValuesBuilder put(ContentValues contentValues) {
    delegate.putAll(contentValues);
    return this;
  }

  public ContentValues build() {
    return new ContentValues(delegate);
  }
}
