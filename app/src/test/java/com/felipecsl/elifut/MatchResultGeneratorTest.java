package com.felipecsl.elifut;

import com.felipecsl.elifut.match.MatchResultGenerator;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.ClubSquad;
import com.felipecsl.elifut.models.GoalGenerator;
import com.felipecsl.elifut.models.MatchResult;
import com.felipecsl.elifut.models.Player;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.felipecsl.elifut.TestFixtures.newGoal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchResultGeneratorTest {
  private final Club home = Club.create(0, "Gremio");
  private final Club away = Club.create(1, "Internacional");
  private final RandomGenerator random = mock(RandomGenerator.class);
  private final RealDistribution distribution = mock(RealDistribution.class);
  private GoalGenerator goalGenerator = mock(GoalGenerator.class);
  private final MatchResultGenerator generator =
      new MatchResultGenerator(goalGenerator, random, distribution);

  @Test public void testSimpleHomeWin() {
    ClubSquad mockSquad = mock(ClubSquad.class);
    when(mockSquad.rating()).thenReturn(70D);
    when(random.nextFloat()).thenReturn(MatchResult.HOME_WIN_PROBABILITY);
    when(distribution.sample()).thenReturn(4.0);
    when(goalGenerator.create(4, home))
        .thenReturn(Arrays.asList(newGoal(home), newGoal(home), newGoal(home), newGoal(home)));
    when(goalGenerator.create(0, away)).thenReturn(Collections.emptyList());

    MatchResult result = generator.generate(home, mockSquad, away, mockSquad);

    assertThat(result.winner()).isEqualTo(home);
    assertThat(result.loser()).isEqualTo(away);
    assertThat(result.finalScore()).isEqualTo("4x0");
    assertThat(result.isDraw()).isEqualTo(false);
    assertThat(result.homeGoals().size()).isEqualTo(4);
    assertThat(result.awayGoals().size()).isEqualTo(0);
  }

  @Test public void testSimpleDraw() {
    ClubSquad mockSquad = mock(ClubSquad.class);
    when(mockSquad.rating()).thenReturn(70D);
    when(random.nextFloat()).thenReturn(
        MatchResult.HOME_WIN_PROBABILITY + MatchResult.DRAW_PROBABILITY);
    when(distribution.sample()).thenReturn(2.0);
    when(goalGenerator.create(1, home)).thenReturn(Collections.singletonList(newGoal(home)));
    when(goalGenerator.create(1, away)).thenReturn(Collections.singletonList(newGoal(away)));

    MatchResult result = generator.generate(home, mockSquad, away, mockSquad);

    assertThat(result.winner()).isEqualTo(null);
    assertThat(result.loser()).isEqualTo(null);
    assertThat(result.finalScore()).isEqualTo("1x1");
    assertThat(result.isDraw()).isEqualTo(true);
    assertThat(result.homeGoals().size()).isEqualTo(1);
    assertThat(result.awayGoals().size()).isEqualTo(1);
  }

  @Test public void testSimpleAwayWin() {
    ClubSquad mockSquad = mock(ClubSquad.class);
    when(mockSquad.rating()).thenReturn(70D);
    when(random.nextFloat()).thenReturn(
        MatchResult.HOME_WIN_PROBABILITY + MatchResult.DRAW_PROBABILITY + 0.1f);
    when(distribution.sample()).thenReturn(1.0);
    when(goalGenerator.create(0, home)).thenReturn(Collections.emptyList());
    when(goalGenerator.create(1, away)).thenReturn(Collections.singletonList(newGoal(away)));

    MatchResult result = generator.generate(home, mockSquad, away, mockSquad);

    assertThat(result.winner()).isEqualTo(away);
    assertThat(result.loser()).isEqualTo(home);
    assertThat(result.finalScore()).isEqualTo("0x1");
    assertThat(result.isDraw()).isEqualTo(false);
    assertThat(result.homeGoals().size()).isEqualTo(0);
    assertThat(result.awayGoals().size()).isEqualTo(1);
  }

  @Ignore("Run only manually")
  @Test public void testStatistics() {
    ClubSquad mockSquad = mock(ClubSquad.class);
    when(mockSquad.rating()).thenReturn(70D);
    MatchResultGenerator generator = new MatchResultGenerator(goalGenerator);
    int totalMatches = 10000;
    float totalHomeWins = 0;
    float totalAwayWins = 0;
    float totalDraws = 0;
    for (int i = 0; i < totalMatches; i++) {
      MatchResult result = generator.generate(
          home, ClubSquad.create(home.id(), newSquad(60)), away, mockSquad);
      if (result.isDraw()) {
        totalDraws++;
      } else if (result.isHomeWin()) {
        totalHomeWins++;
      } else {
        totalAwayWins++;
      }
    }
    System.out.println("Stats after " + totalMatches + " matches:");
    System.out.println("Total home wins: " + (int) ((totalHomeWins / totalMatches) * 100) + "%");
    System.out.println("Total away wins: " + (int) ((totalAwayWins / totalMatches) * 100) + "%");
    System.out.println("Total draws: " + (int) ((totalDraws / totalMatches) * 100) + "%");
  }

  private static Player newPlayer(int rating) {
    return Player.builder()
        .id(1)
        .base_id(1)
        .first_name("Edson")
        .last_name("Arantes do Nascimento")
        .name("Edson Arantes do Nascimento")
        .position("ST")
        .image("http://example.com/foo.png")
        .nation_image("http://example.com/foo.png")
        .rating(rating)
        .clubId(12)
        .player_type("Forward")
        .attribute_1(90)
        .attribute_2(90)
        .attribute_3(90)
        .attribute_4(90)
        .attribute_5(90)
        .attribute_6(90)
        .quality("90")
        .color("gold")
        .build();
  }

  private static List<Player> newSquad(int rating) {
    List<Player> players = new ArrayList<>(11);
    for (int i = 0; i < 10; i++) {
      players.add(newPlayer(i % 2 == 0 ? rating + 1 : rating - 1));
    }
    return players;
  }
}
