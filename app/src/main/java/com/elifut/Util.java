package com.elifut;

import com.elifut.models.Club;
import com.elifut.preferences.LeagueDetails;
import com.elifut.preferences.UserPreferences;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.elifut.services.ResponseBodyMapper;
import com.elifut.services.ResponseMapper;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class Util {
  private Util() {
  }

  public static void closeQuietly(Closeable closeable) {
    try {
      Closeables.close(closeable, true);
    } catch (IOException ignored) {
    }
  }

  public static <T> Class<T> autoValueTypeFor(Class<T> type) {
    try {
      String name = type.getName();
      String packageName = name.substring(0, name.lastIndexOf('.'));
      //noinspection unchecked
      return (Class<T>) Class.forName(packageName + ".AutoValue_" + type.getSimpleName());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> listSupertype(List<? extends T> list) {
    return Lists.transform(list, i -> (T) i);
  }

  public static <T> Observable.Transformer<Response<T>, T> apiObservableTransformer() {
    return (Observable<Response<T>> observable) ->
        observable.subscribeOn(Schedulers.io())
            .flatMap(ResponseMapper.<T>instance())
            .map(ResponseBodyMapper.<T>instance())
            .observeOn(AndroidSchedulers.mainThread());
  }

  public static void defer(Runnable runnable) {
    new AsyncTask<Void, Void, Void>() {
      @Override protected Void doInBackground(Void... params) {
        runnable.run();
        return null;
      }
    }.execute();
  }

  public static void showLeagueEndResults(UserPreferences userPreferences,
      LeagueDetails leagueDetails, Activity activity) {
    Club club = userPreferences.clubPreference().get();
    int position = leagueDetails.clubPosition(club);
    new AlertDialog.Builder(activity)
        .setTitle(R.string.league_ended)
        .setMessage(activity.getString(R.string.you_finished_position, String.valueOf(position)))
        .setOnDismissListener(dialog -> activity.finish())
        .show();
  }

  public static int dpToPx(Context context, int dp) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) ((dp * scale) + 0.5f);
  }
}
