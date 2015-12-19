package com.felipecsl.elifut.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;

public interface Persistable {
  ContentValues toContentValues();

  abstract class Factory<T> {
    //@formatter:off
    private final Type type = new TypeToken<T>(getClass()) {}.getType();

    public abstract String tableName();
    public abstract String createStatement();
    public abstract T fromCursor(Cursor cursor);
    //@formatter:on

    public Class<T> targetType() {
      if (!(type instanceof Class<?>)) {
        throw new IllegalStateException("Type must be Class<?>: " + type);
      }
      //noinspection unchecked
      Class<T> type = (Class<T>) this.type;
      try {
        String name = type.getName();
        String packageName = name.substring(0, name.lastIndexOf('.'));
        //noinspection unchecked
        return (Class<T>) Class.forName(packageName + ".AutoValue_" + type.getSimpleName());
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
