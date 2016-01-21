package com.felipecsl.elifut.models;

import android.content.ContentValues;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.services.ElifutDataStore;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

import static com.felipecsl.elifut.Util.autoValueTypeFor;

public interface Persistable {
  abstract class Converter<T extends Persistable> {
    //@formatter:off
    private final Type type = new TypeToken<T>(getClass()) {}.getType();

    public abstract String tableName();
    public abstract String createStatement();
    public abstract T fromCursor(SimpleCursor cursor, ElifutDataStore service);
    public abstract ContentValues toContentValues(T persistable, ElifutDataStore service);
    //@formatter:on

    public Class<T> targetType() {
      if (!(type instanceof Class<?>)) {
        throw new IllegalStateException("Type must be Class<?>: " + type);
      }
      //noinspection unchecked
      return (Class<T>) autoValueTypeFor((Class<?>) this.type);
    }
  }
}
