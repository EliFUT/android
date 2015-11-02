package com.felipecsl.elifut.models;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public final class Goals {
  public static List<Goal> create(RandomGenerator random, int qty, Club club) {
    List<Goal> goals = new ArrayList<>(qty);
    for (int i = 0; i < qty; i++) {
      goals.add(Goal.create(random.nextInt(90), club));
    }
    return goals;
  }
}
