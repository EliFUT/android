package com.felipecsl.elifut;

import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.MatchResult;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchResultGeneratorTest {
  private final Club home = Club.create(0, "Gremio");
  private final Club away = Club.create(1, "Internacional");
  private final RandomGenerator random = mock(RandomGenerator.class);
  private final RealDistribution distribution = mock(RealDistribution.class);
  private final MatchResultGenerator generator = new MatchResultGenerator(random, distribution);

  @Test public void testSimpleHomeWin() {
    when(random.nextFloat()).thenReturn(MatchResult.HOME_WIN_PROBABILITY);
    when(distribution.sample()).thenReturn(4.0);

    MatchResult result = generator.generate(home, away);

    assertThat(result.winner()).isEqualTo(home);
    assertThat(result.loser()).isEqualTo(away);
    assertThat(result.finalScore()).isEqualTo("4x0");
    assertThat(result.isDraw()).isEqualTo(false);
    assertThat(result.homeGoals().size()).isEqualTo(4);
    assertThat(result.awayGoals().size()).isEqualTo(0);
  }

  @Test public void testSimpleDraw() {
    when(random.nextFloat()).thenReturn(MatchResult.DRAW_PROBABILITY);
    when(distribution.sample()).thenReturn(2.0);

    MatchResult result = generator.generate(home, away);

    assertThat(result.winner()).isEqualTo(null);
    assertThat(result.loser()).isEqualTo(null);
    assertThat(result.finalScore()).isEqualTo("1x1");
    assertThat(result.isDraw()).isEqualTo(true);
    assertThat(result.homeGoals().size()).isEqualTo(1);
    assertThat(result.awayGoals().size()).isEqualTo(1);
  }

  @Test public void testSimpleAwayWin() {
    when(random.nextFloat()).thenReturn(MatchResult.DRAW_PROBABILITY + 0.1f);
    when(distribution.sample()).thenReturn(1.0);

    MatchResult result = generator.generate(home, away);

    assertThat(result.winner()).isEqualTo(away);
    assertThat(result.loser()).isEqualTo(home);
    assertThat(result.finalScore()).isEqualTo("0x1");
    assertThat(result.isDraw()).isEqualTo(false);
    assertThat(result.homeGoals().size()).isEqualTo(0);
    assertThat(result.awayGoals().size()).isEqualTo(1);
  }
}
