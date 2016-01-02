package com.felipecsl.elifut.services;

import android.os.Build;

import com.felipecsl.elifut.AutoValueClasses;
import com.felipecsl.elifut.BuildConfig;
import com.felipecsl.elifut.ElifutTestRunner;
import com.felipecsl.elifut.TestElifutApplication;
import com.felipecsl.elifut.TestUtil;
import com.felipecsl.elifut.models.Club;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ElifutTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
    manifest = ElifutTestRunner.MANIFEST_PATH)
public class ElifutPersistenceServiceTest {
  @Inject ElifutPersistenceService service;

  @Before public void setUp() {
    TestElifutApplication application = (TestElifutApplication) RuntimeEnvironment.application;
    application.testComponent().inject(this);
  }

  @Test public void testQueryEmptyData() {
    assertThat(service.query(AutoValueClasses.CLUB)).isEqualTo(Collections.emptyList());
  }

  @Test public void testQueryById() {
    List<Club> clubs = Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.queryOne(AutoValueClasses.CLUB, TestUtil.GREMIO.id()))
        .isEqualTo(TestUtil.GREMIO);
    assertThat(service.queryOne(AutoValueClasses.CLUB, TestUtil.INTERNACIONAL.id()))
        .isEqualTo(TestUtil.INTERNACIONAL);
  }

  @Test public void testObservable() {
    List<Club> clubs = Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL);
    service.create(clubs);
    TestSubscriber<List<? extends Club>> subscriber = new TestSubscriber<>();
    service.observable(AutoValueClasses.CLUB).subscribe(subscriber);
    subscriber.assertNoErrors();
    subscriber.assertValue(clubs);
  }

  @Test public void testUpdate() {
    List<Club> clubs = Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL);
    service.create(clubs);
    Club updatedGremio = TestUtil.GREMIO.toBuilder().small_image("plimba").build();
    assertThat(service.update(updatedGremio, updatedGremio.id())).isEqualTo(1);
    assertThat(service.query(AutoValueClasses.CLUB))
        .isEqualTo(Arrays.asList(updatedGremio, TestUtil.INTERNACIONAL));
  }

  @Test public void testDelete() {
    List<Club> clubs = Arrays.asList(TestUtil.GREMIO, TestUtil.INTERNACIONAL);
    service.create(clubs);
    assertThat(service.delete(TestUtil.GREMIO, TestUtil.GREMIO.id())).isEqualTo(1);
    assertThat(service.query(AutoValueClasses.CLUB))
        .isEqualTo(Collections.singletonList(TestUtil.INTERNACIONAL));
  }
}