package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Club;

public final class TestUtil {
  public static final Club GREMIO = Club.builder()
      .name("Gremio")
      .small_image("abc")
      .large_image("def")
      .league_id(1)
      .id(1)
      .base_id(1)
      .build();
  public static final Club INTERNACIONAL = Club.builder()
      .name("Internacional")
      .small_image("xxx")
      .large_image("xyz")
      .league_id(4)
      .id(2)
      .base_id(3)
      .build();

  public static Club newClub(String name) {
    return newClub(1, name);
  }

  public static Club newClub(int id, String name) {
    return Club.builder()
        .name(name)
        .small_image("")
        .large_image("")
        .league_id(1)
        .id(id)
        .base_id(1)
        .build();
  }
}
