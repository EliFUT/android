package com.felipecsl.elifut.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.felipecsl.elifut.SimpleCursor;
import com.felipecsl.elifut.models.Persistable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.felipecsl.elifut.Util.closeQuietly;
import static com.google.common.base.Preconditions.checkNotNull;

public class ElifutPersistenceService extends SQLiteOpenHelper {
  private final BriteDatabase db;
  private final Map<Class<?>, Persistable.Converter<?>> converterMap = new HashMap<>();

  public ElifutPersistenceService(Context context, SqlBrite sqlBrite,
      List<Persistable.Converter<?>> factories) {
    super(context, "ElifutDB", null, 1);
    db = sqlBrite.wrapDatabaseHelper(this);
    for (Persistable.Converter<?> converter : factories) {
      converterMap.put(converter.targetType(), converter);
    }
  }

  @Override public void onCreate(SQLiteDatabase db) {
    for (Persistable.Converter<?> converter : converterMap.values()) {
      db.execSQL(converter.createStatement());
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }

  public void create(List<? extends Persistable> persistables) {
    BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (Persistable persistable : persistables) {
        Class<? extends Persistable> type = persistable.getClass();
        //noinspection rawtypes
        Persistable.Converter converter = converterForType(type);
        //noinspection unchecked
        db.insert(converter.tableName(), converter.toContentValues(persistable, this));
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }

  public <T extends Persistable> List<T> query(Class<T> type) {
    Cursor cursor = null;
    List<T> items = new ArrayList<>();
    Persistable.Converter<T> converter = converterForType(type);
    try {
      cursor = db.query("SELECT * FROM " + converter.tableName());
      while (cursor.moveToNext()) {
        items.add(cursorToObject(converter, cursor));
      }
    } finally {
      closeQuietly(cursor);
    }
    return items;
  }

  public <T extends Persistable> Observable<T> observable(Class<T> type) {
    Persistable.Converter<T> converter = converterForType(type);
    String tableName = converter.tableName();
    return db.createQuery(tableName, "SELECT * FROM " + tableName)
        .mapToList(cursor -> cursorToObject(converter, cursor))
        .flatMap(Observable::from);
  }

  @Nullable public <T extends Persistable> T query(Class<T> type, int rowId) {
    Cursor cursor = null;
    Persistable.Converter<T> converter = converterForType(type);
    try {
      cursor = db.query("SELECT * FROM " + converter.tableName() + " WHERE id = ?",
          String.valueOf(rowId));
      if (cursor.moveToNext()) {
        return cursorToObject(converter, cursor);
      }
    } finally {
      closeQuietly(cursor);
    }
    return null;
  }

  public <T extends Persistable> Persistable.Converter<T> converterForType(Class<?> type) {
    //noinspection unchecked
    return checkNotNull(
        (Persistable.Converter<T>) converterMap.get(type), "No factory found for type " + type);
  }

  private <T extends Persistable> T cursorToObject(
      Persistable.Converter<T> converter, Cursor cursor) {
    return converter.fromCursor(new SimpleCursor(cursor), this);
  }
}
