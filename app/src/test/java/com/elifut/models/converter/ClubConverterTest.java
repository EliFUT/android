package com.elifut.models.converter;

import com.elifut.AutoValueClasses;
import com.elifut.BuildConfig;
import com.elifut.TestElifutApplication;
import com.elifut.TestFixtures;
import com.elifut.models.Club;
import com.elifut.services.ElifutDataStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ClubConverterTest {
  @Inject ElifutDataStore service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testPersistence() {
    List<Club> clubs = Arrays.asList(TestFixtures.GREMIO, TestFixtures.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.query(AutoValueClasses.CLUB)).isEqualTo(clubs);
  }
}