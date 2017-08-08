package com.elifut

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.elifut.activity.CurrentTeamDetailsActivity
import com.elifut.activity.MainActivity
import com.elifut.activity.MatchProgressActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okreplay.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LeagueTest {
  private val activityTestRule = ActivityTestRule(MainActivity::class.java)
  private val configuration = OkReplayConfig.Builder()
      .tapeRoot(AndroidTapeRoot(InstrumentationRegistry.getContext(), javaClass))
      .defaultMode(TapeMode.READ_ONLY)
      .sslEnabled(true)
      .interceptor(OkReplayInterceptorProvider.instance)
      .build()
  @JvmField @Rule val testRule = OkReplayRuleChain(configuration, activityTestRule).get()
  private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

  @Before fun setUp() {
    Intents.init()
    val component = application().component
    val okHttpClient = component.okHttpClient()
    component.appInitializer().clearData()
    okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
    IdlingRegistry.getInstance().register(okHttp3IdlingResource)
  }

  @After fun tearDown() {
    Intents.release()
    IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
  }

  @Test
  @OkReplay
  fun testLeague() {
    onView(withText("Argentina")).perform(click())
    onView(withId(R.id.fab)).perform(click())
    onView(withText(R.string.loading)).check(matches(isDisplayed()))
    Thread.sleep(3000)
    intended(hasComponent(CurrentTeamDetailsActivity::class.java.name))
    onView(withText(R.string.team_info)).check(matches(isDisplayed()))
    onView(withText(R.string.league)).check(matches(isDisplayed()))
    onView(withContentDescription("Open navigation drawer")).perform(click())
    onView(withText(R.string.current_league)).perform(click())
    onView(withText(R.string.schedule)).check(matches(isDisplayed()))
    onView(withText(R.string.standings)).check(matches(isDisplayed()))
    val totalRounds = 35
    val gameSpeed = 20
    application().component.userPreferences().gameSpeedPreference().set(gameSpeed)
    1.until(totalRounds).forEach {
      onView(withId(R.id.fab)).perform(click())
      Thread.sleep(((45 + 45) / gameSpeed) * 1000L)
      // Wait for observables to catch up
      Thread.sleep(1000)
      onView(withText(R.string.end_first_half)).check(matches(isDisplayed()))
      onView(withText(R.string.end_match)).check(matches(isDisplayed()))
      onView(anyOf(
          withText(R.string.defeated),
          withText(R.string.winner),
          withText(R.string.draw))).check(matches(isDisplayed()))
      onView(withId(R.id.fab_done)).perform(click())
      onView(withId(R.id.fab)).perform(click())
    }
  }

  private fun application() =
      activityTestRule.activity.applicationContext as ElifutApplication
}