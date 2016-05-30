package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Goal;
import com.felipecsl.elifut.models.Player;

public final class TestFixtures {
  private TestFixtures() {
  }

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

  public static final Player PELE = Player.builder()
      .id(1)
      .base_id(1)
      .first_name("Edson")
      .last_name("Arantes do Nascimento")
      .name("Edson Arantes do Nascimento")
      .position("ST")
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

  public static final Player GORNALDO = Player.builder()
      .id(2)
      .base_id(2)
      .first_name("Ronaldo")
      .last_name("Nazario")
      .name("Ronaldo Nazario")
      .position("ST")
      .image("http://example.com/foo.png")
      .nation_image("http://example.com/foo.png")
      .rating(90)
      .clubId(13)
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

  public static Goal newGoal(Club club) {
    return newGoal(5, club);
  }

  public static Goal newGoal(int id, Club club) {
    return newGoal(id, club, PELE);
  }

  public static Goal newGoal(int id, Club club, Player player) {
    return Goal.create(id, club, player);
  }
}
