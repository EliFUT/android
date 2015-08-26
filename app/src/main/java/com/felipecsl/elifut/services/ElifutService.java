package com.felipecsl.elifut.services;

import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.models.Player;

import java.util.List;

import retrofit.Response;
import retrofit.http.GET;
import rx.Observable;

public interface ElifutService {
  @GET("/players/{id}.json") Observable<Response<Player>> player(int id);

  @GET("/nations.json") Observable<Response<List<Nation>>> nations();
}
