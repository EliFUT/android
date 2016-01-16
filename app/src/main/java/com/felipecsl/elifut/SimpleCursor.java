package com.felipecsl.elifut;

import android.database.Cursor;

public class SimpleCursor {
  private final Cursor cursor;

  public SimpleCursor(Cursor cursor) {
    this.cursor = cursor;
  }

  public int getInt(String columnName) {
    return cursor.getInt(cursor.getColumnIndex(columnName));
  }

  public String getString(String columnName) {
    return cursor.getString(cursor.getColumnIndex(columnName));
  }

  public Cursor toCursor() {
    return cursor;
  }
}
