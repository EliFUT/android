package com.elidroid;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

final class ElidroidDatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "Elidroid";
  private static final int DATABASE_VERSION = 1;
  private static final String[] COLUMNS =
      new String[] { "id", "baseId", "resourceId", "First_Name", "Last_Name", "Common_Name",
          "Rating", "Rare", "Foot", "Height", "Date_Of_Birth", "ClubId", "NationId", "LeagueId",
          "Attribute1", "Attribute2", "Attribute3", "Attribute4", "Attribute5", "Attribute6" };

  private static final String COL_ID = "id";
  private static final String COL_BASEID = "baseId";
  private static final String COL_RESOURCEID = "resourceId";
  private static final String COL_FIRST_NAME = "First_Name";
  private static final String COL_LAST_NAME = "Last_Name";
  private static final String COL_COMMON_NAME = "Common_Name";
  private static final String COL_RATING = "Rating";
  private static final String COL_RARE = "Rare";
  private static final String COL_FOOT = "Foot";
  private static final String COL_HEIGHT = "Height";
  private static final String COL_DATE_OF_BIRTH = "Date_Of_Birth";
  private static final String COL_CLUBID = "ClubId";
  private static final String COL_NATIONID = "NationId";
  private static final String COL_LEAGUEID = "LeagueId";
  private static final String COL_ATTRIBUTE1 = "Attribute1";
  private static final String COL_ATTRIBUTE2 = "Attribute2";
  private static final String COL_ATTRIBUTE3 = "Attribute3";
  private static final String COL_ATTRIBUTE4 = "Attribute4";
  private static final String COL_ATTRIBUTE5 = "Attribute5";
  private static final String COL_ATTRIBUTE6 = "Attribute6";

  private final Context context;

  public ElidroidDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.context = context;
  }

  @Override public void onCreate(SQLiteDatabase db) {
    Resources resources = context.getResources();
    InputStream stream = resources.openRawResource(R.raw.createdb);
    try {
      db.execSQL(IOUtils.toString(stream));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }

  List<Player> players() {
    SQLiteDatabase database = getReadableDatabase();
    Cursor cursor = database.query("fut15_players", COLUMNS, null, null, null, null, null);
    List<Player> players = new ArrayList<>();

    while (cursor.moveToNext()) {
      int id = getInt(cursor, COL_ID);
      int baseId = getInt(cursor COL_BASEID);
      int resourceId = getInt(cursor, COL_RESOURCEID);
      String firstName = getString(cursor, COL_FIRST_NAME);
      String lastName = getString(cursor, COL_LAST_NAME);
      String commonName = getString(cursor, COL_COMMON_NAME);
      int rating = getInt(cursor, COL_RATING);
      boolean isRare = getInt(cursor, COL_RARE) == 1;
      Player.Foot foot = Player.Foot.valueOf(getString(cursor, COL_FOOT));
      int height = getInt(cursor, COL_HEIGHT);
      String birthDate = getString(cursor, COL_DATE_OF_BIRTH);
      int clubId = getInt(cursor, COL_CLUBID);
      int nationId = getInt(cursor, COL_NATIONID);
      int leagueId = getInt(cursor, COL_LEAGUEID);
      int attribute1 = getInt(cursor, COL_ATTRIBUTE1);
      int attribute2 = getInt(cursor, COL_ATTRIBUTE2);
      int attribute3 = getInt(cursor, COL_ATTRIBUTE3);
      int attribute4 = getInt(cursor, COL_ATTRIBUTE4);
      int attribute5 = getInt(cursor, COL_ATTRIBUTE5);
      int attribute6 = getInt(cursor, COL_ATTRIBUTE6);

      players.add(new Player(id, baseId, resourceId, firstName, lastName, commonName, rating,
          isRare, foot, height, birthDate, club, nattion, league, attribute1, attribute2,
          attribute3, attribute4, attribute5, attribute6));
    }

    return players;
  }

  private int getInt(Cursor cursor, String column) {
    return cursor.getInt(cursor.getColumnIndex(column));
  }

  private String getString(Cursor cursor, String column) {
    return cursor.getString(cursor.getColumnIndex(column));
  }
}
