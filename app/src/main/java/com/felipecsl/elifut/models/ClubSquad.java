package com.felipecsl.elifut.models;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ClubSquad implements Persistable {
  @Nullable public abstract Integer id();
  public abstract int clubId();
  public abstract List<Player> squad();

  public static ClubSquad create(int clubId, List<Player> squad) {
    return create(null, clubId, squad);
  }

  public static ClubSquad create(Integer id, int clubId, List<Player> squad) {
    return new AutoValue_ClubSquad(id, clubId, squad);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder id(Integer x);
    public abstract Builder clubId(int x);
    public abstract Builder squad(List<Player> x);
    public abstract ClubSquad build();
  }

  public static Builder builder() {
    return new AutoValue_ClubSquad.Builder();
  }

  public Builder toBuilder() {
    // https://github.com/google/auto/issues/281
    return new AutoValue_ClubSquad.Builder(this);
  }
}
