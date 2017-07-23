package com.elifut;

import com.elifut.models.Club;

public final class TestUtil {
  private TestUtil() {
  }

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
