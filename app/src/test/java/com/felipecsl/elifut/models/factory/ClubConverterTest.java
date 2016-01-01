package com.felipecsl.elifut.models.factory;

import android.os.Build;

import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestUtil;
import com.felipecsl.elifut.Util;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.services.ElifutPersistenceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class ClubConverterTest {
  @Inject ElifutPersistenceService service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testPersistence() {
    List<Club> clubs = Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.query(Util.autoValueTypeFor(Club.class))).isEqualTo(clubs);
  }
}