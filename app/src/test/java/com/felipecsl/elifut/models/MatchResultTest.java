package com.felipecsl.elifut.models;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchResultTest {
  @Test public void testBuilderHomeWin() {
    Club home = Club.create(1, "Gremio");
    Club away = Club.create(2, "Internacional");
    List<Goal> homeGoals = Collections.singletonList(Goal.create(5, home));

    MatchResult matchResult = MatchResult.builder()
        .homeGoals(homeGoals)
        .build(home, away);

    assertThat(matchResult.isDraw()).isEqualTo(false);
    assertThat(matchResult.isHomeWin()).isEqualTo(true);
    assertThat(matchResult.isAwayWin()).isEqualTo(false);
    assertThat(matchResult.homeGoals()).isEqualTo(homeGoals);
    assertThat(matchResult.awayGoals()).isEqualTo(Collections.emptyList());
    assertThat(matchResult.winner()).isEqualTo(home);
    assertThat(matchResult.loser()).isEqualTo(away);
  }

  @Test public void testBuilderDraw() {
    Club home = Club.create(1, "Gremio");
    Club away = Club.create(2, "Internacional");

    MatchResult matchResult = MatchResult.builder()
        .build(home, away);

    assertThat(matchResult.isDraw()).isEqualTo(true);
    assertThat(matchResult.isHomeWin()).isEqualTo(false);
    assertThat(matchResult.isAwayWin()).isEqualTo(false);
    assertThat(matchResult.homeGoals()).isEqualTo(Collections.emptyList());
    assertThat(matchResult.awayGoals()).isEqualTo(Collections.emptyList());
    assertThat(matchResult.winner()).isEqualTo(null);
    assertThat(matchResult.loser()).isEqualTo(null);
  }
}