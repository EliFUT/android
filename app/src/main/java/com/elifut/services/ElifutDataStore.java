package com.elifut.services;

import com.google.common.primitives.Ints;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.elifut.SimpleCursor;
import com.elifut.models.Persistable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.elifut.Util.closeQuietly;
import static com.google.common.base.Preconditions.checkNotNull;

public class ElifutDataStore extends SQLiteOpenHelper {
  private final BriteDatabase db;
  private final Map<Class<?>, Persistable.Converter<?>> converterMap = new HashMap<>();

  public ElifutDataStore(Context context, SqlBrite sqlBrite,
      List<Persistable.Converter<?>> converters) {
    super(context, "ElifutDB", null, 1);
    // TODO: Change to io() or computation() scheduler but needs to fix failing test
    db = sqlBrite.wrapDatabaseHelper(this, AndroidSchedulers.mainThread());
    for (Persistable.Converter<?> converter : converters) {
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

  public long create(Persistable persistable) {
    Persistable.Converter<Persistable> converter = converterFor(persistable);
    return db.insert(converter.tableName(), converter.toContentValues(persistable, this));
  }

  public void create(Persistable... persistables) {
    create(Arrays.asList(persistables));
  }

  public void create(List<? extends Persistable> persistables) {
    BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      for (Persistable persistable : persistables) {
        create(persistable);
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }

  public int update(Persistable persistable, int id) {
    Persistable.Converter<Persistable> converter = converterFor(persistable);
    return db.update(converter.tableName(), converter.toContentValues(persistable, this),
        "id = ?", String.valueOf(id));
  }

  public int delete(Persistable persistable, int id) {
    Persistable.Converter<Persistable> converter = converterFor(persistable);
    return db.delete(converter.tableName(), "id = ?", String.valueOf(id));
  }

  public <T extends Persistable> List<T> query(Class<T> type) {
    Persistable.Converter<T> converter = converterForType(type);
    return rawQuery(converter, "SELECT * FROM " + converter.tableName());
  }

  public <T extends Persistable> List<T> query(Class<T> type, int... rowIds) {
    Persistable.Converter<T> converter = converterForType(type);
    return rawQuery(converter, "SELECT * FROM " + converter.tableName() + " WHERE id IN ( "
        + TextUtils.join(", ", Ints.asList(rowIds)) + ")");
  }

  public <T extends Persistable> List<T> query(Class<T> type, String whereClause, String... args) {
    Persistable.Converter<T> converter = converterForType(type);
    return rawQuery(converter, "SELECT * FROM " + converter.tableName() + " WHERE " + whereClause,
        args);
  }

  private <T extends Persistable> List<T> rawQuery(
      Persistable.Converter<T> converter, String query, String... args) {
    Cursor cursor = null;
    List<T> items = new ArrayList<>();
    try {
      cursor = db.query(query, args);
      while (cursor.moveToNext()) {
        items.add(cursorToObject(converter, cursor));
      }
    } finally {
      closeQuietly(cursor);
    }
    return items;
  }

  @Nullable public <T extends Persistable> T queryOne(Class<T> type, String whereClause,
      String... args) {
    Persistable.Converter<T> converter = converterForType(type);
    List<T> list = rawQuery(
        converter, "SELECT * FROM " + converter.tableName() + " WHERE " + whereClause, args);
    if (!list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  @Nullable public <T extends Persistable> T queryOne(Class<T> type, int rowId) {
    return queryOne(type, "id = ?", String.valueOf(rowId));
  }

  public <T extends Persistable> Observable<List<T>> observe(Class<T> type) {
    Persistable.Converter<T> converter = converterForType(type);
    String tableName = converter.tableName();
    return db.createQuery(tableName, "SELECT * FROM " + tableName)
        .mapToList(cursor -> cursorToObject(converter, cursor));
  }

  public <T extends Persistable> Observable<List<T>> observe(
      Class<T> type, String whereClause, String... args) {
    Persistable.Converter<T> converter = converterForType(type);
    String tableName = converter.tableName();
    return db.createQuery(tableName, "SELECT * FROM " + tableName + " WHERE " + whereClause, args)
        .mapToList(cursor -> cursorToObject(converter, cursor));
  }

  public <T extends Persistable> Observable<T> observeOne(
      Class<T> type, String whereClause, String... args) {
    Persistable.Converter<T> converter = converterForType(type);
    String tableName = converter.tableName();
    return db.createQuery(tableName, "SELECT * FROM " + tableName + " WHERE " + whereClause, args)
        .mapToOne(cursor -> cursorToObject(converter, cursor));
  }

  public <T extends Persistable> Persistable.Converter<T> converterFor(Persistable persistable) {
    return converterForType(persistable.getClass());
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

  public void deleteAll() {
    for (Persistable.Converter<?> converter : converterMap.values()) {
      db.execute("DELETE FROM " + converter.tableName());
    }
  }
}
