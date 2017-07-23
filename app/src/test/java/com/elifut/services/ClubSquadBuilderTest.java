package com.elifut.services;

import com.elifut.TestFixtures;
import com.elifut.models.ClubSquad;
import com.elifut.models.Player;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubSquadBuilderTest {
  @Test public void throwIfNull() {
    try {
      ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, null);
      builder.build();
      fail();
    } catch (NullPointerException ignored) {
    }
  }

  @Test public void throwIfNotEnoughPlayers() {
    try {
      ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Collections.emptyList());
      builder.build();
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test public void throwIfNotEnoughGoalkeepers() {
    try {
      List<Player> players = Arrays.asList(buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
          buildPlayer("RB"), buildPlayer("LM"), buildPlayer("CM"), buildPlayer("CM"),
          buildPlayer("CAM"), buildPlayer("RM"), buildPlayer("ST"), buildPlayer("CF"));
      ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, players);
      builder.build();
      fail();
    } catch (IllegalArgumentException ignored) {
    }
  }

  @Test public void simpleSquad() {
    Player[] players = new Player[] { buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
        buildPlayer("RB"), buildPlayer("LM"), buildPlayer("CM"), buildPlayer("GK"),
        buildPlayer("CAM"), buildPlayer("CDM"), buildPlayer("ST"), buildPlayer("CF") };
    ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Arrays.asList(players));

    ClubSquad clubSquad = builder.build();

    assertThat(clubSquad.players()).containsExactly(buildPlayer("GK"), buildPlayer("LB"),
        buildPlayer("CB"), buildPlayer("CB"), buildPlayer("RB"), buildPlayer("LM"),
        buildPlayer("CM"), buildPlayer("CAM"), buildPlayer("CDM"), buildPlayer("ST"),
        buildPlayer("CF"));
  }

  @Test public void bigSquad() {
    Player[] players = new Player[] { buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
        buildPlayer("CB"), buildPlayer("RB"), buildPlayer("LM"), buildPlayer("CM"),
        buildPlayer("GK"), buildPlayer("GK"), buildPlayer("CAM"), buildPlayer("CDM"),
        buildPlayer("ST"), buildPlayer("CF"), buildPlayer("ST") };
    ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Arrays.asList(players));

    ClubSquad clubSquad = builder.build();

    assertThat(clubSquad.players()).containsExactly(buildPlayer("GK"), buildPlayer("LB"),
        buildPlayer("CB"), buildPlayer("CB"), buildPlayer("CB"), buildPlayer("LM"),
        buildPlayer("CM"), buildPlayer("CAM"), buildPlayer("CDM"), buildPlayer("ST"),
        buildPlayer("CF"));
  }

  @Test public void noAttackersButExtraMidfielders() {
    Player[] players = new Player[] { buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
        buildPlayer("CM"), buildPlayer("RB"), buildPlayer("LM"), buildPlayer("CM"),
        buildPlayer("GK"), buildPlayer("CM"), buildPlayer("CAM"), buildPlayer("CDM") };
    ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Arrays.asList(players));

    ClubSquad clubSquad = builder.build();

    assertThat(clubSquad.players()).containsExactly(buildPlayer("GK"), buildPlayer("LB"),
        buildPlayer("CB"), buildPlayer("CB"), buildPlayer("RB"), buildPlayer("CM"),
        buildPlayer("LM"), buildPlayer("CM"), buildPlayer("CM"), buildPlayer("CAM"),
        buildPlayer("CDM"));
  }

  @Test public void noAttackersButExtraDefenderAndGoalkeeper() {
    Player[] players = new Player[] { buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
        buildPlayer("CB"), buildPlayer("RB"), buildPlayer("LM"), buildPlayer("CM"),
        buildPlayer("GK"), buildPlayer("GK"), buildPlayer("CAM"), buildPlayer("CDM") };
    ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Arrays.asList(players));

    ClubSquad clubSquad = builder.build();

    assertThat(clubSquad.players()).containsExactly(buildPlayer("GK"), buildPlayer("LB"),
        buildPlayer("CB"), buildPlayer("CB"), buildPlayer("CB"), buildPlayer("LM"),
        buildPlayer("CM"), buildPlayer("CAM"), buildPlayer("CDM"), buildPlayer("RB"),
        buildPlayer("GK"));
  }

  @Test public void notEnoughDefendersButExtraMidfielders() {
    Player[] players = new Player[] { buildPlayer("LB"), buildPlayer("CB"), buildPlayer("CB"),
        buildPlayer("CDM"), buildPlayer("RW"), buildPlayer("LM"), buildPlayer("CM"),
        buildPlayer("GK"), buildPlayer("ST"), buildPlayer("CAM"), buildPlayer("CDM") };
    ClubSquadBuilder builder = new ClubSquadBuilder(TestFixtures.GREMIO, Arrays.asList(players));

    ClubSquad clubSquad = builder.build();

    assertThat(clubSquad.players()).containsExactly(buildPlayer("GK"), buildPlayer("LB"),
        buildPlayer("CB"), buildPlayer("CB"), buildPlayer("CDM"), buildPlayer("LM"),
        buildPlayer("CM"), buildPlayer("CAM"), buildPlayer("CDM"), buildPlayer("RW"),
        buildPlayer("ST"));
  }

  private static Player buildPlayer(String position) {
    return Player.builder()
        .id(1)
        .base_id(1)
        .first_name("Pele")
        .last_name("N")
        .name("Pele")
        .position(position)
        .image("http://example.com/foo.png")
        .nation_image("http://example.com/foo.png")
        .rating(90)
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
}