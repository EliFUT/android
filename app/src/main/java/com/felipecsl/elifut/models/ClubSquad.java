package com.felipecsl.elifut.models;

import com.google.auto.value.AutoValue;
import com.google.common.collect.FluentIterable;

import android.support.annotation.Nullable;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

@AutoValue
public abstract class ClubSquad implements Persistable {
  @Nullable public abstract Integer id();
  public abstract int clubId();
  public abstract List<Player> players();

  // TODO: For now, all squads are 4-4-2 by default
  public static final int TOTAL_DEFENDERS = 4;
  public static final int TOTAL_MIDFIELDERS = 4;
  public static final int TOTAL_ATTACKERS = 2;

  public static ClubSquad create(int clubId, List<Player> squad) {
    return create(null, clubId, squad);
  }

  public static ClubSquad create(Integer id, int clubId, List<Player> squad) {
    return new AutoValue_ClubSquad(id, clubId, squad);
  }

  public double rating() {
    List<Integer> ratings = FluentIterable.from(players()).transform(Player::rating).toList();
    DescriptiveStatistics stats = new DescriptiveStatistics();
    for (Integer rating : ratings) {
      stats.addValue(rating);
    }
    double mean = stats.getMean();
    double standardDeviation = stats.getStandardDeviation();
    NormalDistribution normalDistribution = new NormalDistribution(mean, standardDeviation);
    return normalDistribution.sample();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder id(Integer x);
    public abstract Builder clubId(int x);
    public abstract Builder players(List<Player> x);
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
