package com.felipecsl.elifut.services;

import com.felipecsl.elifut.models.Player;

import retrofit.Response;
import retrofit.http.GET;
import rx.Observable;

public interface ElifutService {
  @GET Observable<Response<Player>> player(int id);
}
