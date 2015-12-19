package com.felipecsl.elifut.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.felipecsl.elifut.models.Persistable;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.felipecsl.elifut.Util.closeQuietly;
import static com.google.common.base.Preconditions.checkNotNull;

public class ElifutPersistenceService extends SQLiteOpenHelper {
  private final BriteDatabase db;
  private final Map<Class<?>, Persistable.Factory<?>> factoryMap = new HashMap<>();

  public ElifutPersistenceService(Context context, SqlBrite sqlBrite,
      List<Persistable.Factory<?>> factories) {
    super(context, "ElifutDB", null, 1);
    db = sqlBrite.wrapDatabaseHelper(this);
    for (Persistable.Factory<?> factory : factories) {
      factoryMap.put(factory.targetType(), factory);
    }
  }

  @Override public void onCreate(SQLiteDatabase db) {
    for (Persistable.Factory<?> factory : factoryMap.values()) {
      db.execSQL(factory.createStatement());
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public void create(List<? extends Persistable> persistables) {
    BriteDatabase.Transaction transaction = db.newTransaction();
    try {
      transaction.markSuccessful();
      for (Persistable persistable : persistables) {
        Class<? extends Persistable> type = persistable.getClass();
        Persistable.Factory<?> factory = checkNotNull(
            factoryMap.get(type), "No factory found for type " + type);
        db.insert(factory.tableName(), persistable.toContentValues());
      }
    } finally {
      transaction.end();
    }
  }

  public <T extends Persistable> List<T> query(Class<T> type) {
    Cursor cursor = null;
    List<T> items = new ArrayList<>();
    //noinspection unchecked
    Persistable.Factory<T> factory = checkNotNull(
        (Persistable.Factory<T>) factoryMap.get(type), "No factory found for type " + type);
    try {
      cursor = db.query("SELECT * FROM clubs");
      while (cursor.moveToNext()) {
        items.add(factory.fromCursor(cursor));
      }
    } finally {
      closeQuietly(cursor);
    }
    return items;
  }
}
