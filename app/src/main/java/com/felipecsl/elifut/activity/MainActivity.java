package com.felipecsl.elifut.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.felipecsl.elifut.AppInitializer;
import com.felipecsl.elifut.CompletionObserver;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.ResponseObserver;
import com.felipecsl.elifut.adapter.CountriesSpinnerAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.GoogleApiConnectionResult;
import com.felipecsl.elifut.models.Nation;
import com.felipecsl.elifut.preferences.UserPreferences;
import com.felipecsl.elifut.services.GoogleApiConnectionHandler;
import com.felipecsl.elifut.services.GoogleApiConnectionHandlerFactory;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Player;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.NotImplementedError;
import rx.Observer;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends ElifutActivity {
  private static final String TAG = "MainActivity";

  @Inject AppInitializer initializer;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @BindView(R.id.countries_spinner) Spinner countriesSpinner;
  @BindView(R.id.fab) FloatingActionButton okButton;
  @BindView(R.id.sign_in_button) SignInButton signInButton;
  @BindView(R.id.main_content) CoordinatorLayout mainContent;

  private GoogleApiConnectionHandler googleApiConnectionHandler;
  private String displayName;
  private CountriesSpinnerAdapter nationsAdapter;

  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private final Observer<List<Nation>> nationObserver =
      new ResponseObserver<List<Nation>>(this, TAG, "Failed to load list of countries.") {
        @Override public void onNext(List<Nation> response) {
          nationsAdapter = new CountriesSpinnerAdapter(MainActivity.this, response);
          countriesSpinner.setAdapter(nationsAdapter);
        }
      };
  private final SingleSubscriber<GoogleApiConnectionResult> playerSubscriber =
      new SingleSubscriber<GoogleApiConnectionResult>() {
        @Override public void onSuccess(GoogleApiConnectionResult connectionResult) {
          signInButton.setVisibility(View.GONE);
          Player player = connectionResult.getPlayer();
          if (player == null) {
            Log.w(TAG, "gamesClient.getCurrentPlayer() is NULL!");
            displayName = "???";
          } else {
            displayName = player.getDisplayName();
          }
          Snackbar.make(mainContent, "Welcome, " + displayName, Snackbar.LENGTH_SHORT).show();
        }

        @Override public void onError(Throwable error) {
          if (!(error instanceof NotImplementedError)) {
            Snackbar.make(mainContent, error.getMessage(), Snackbar.LENGTH_LONG).show();
          }
        }
      };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);
    googleApiConnectionHandler = GoogleApiConnectionHandlerFactory.Companion.newInstance(this);
    subscriptions.add(googleApiConnectionHandler.result().subscribe(playerSubscriber));
    Nation nation = userPreferences.nationPreference().get();

    if (nation != null) {
      launchHomeScreen();
    } else {
      subscriptions.add(service.nations()
          .compose(this.<List<Nation>>applyTransformations())
          .subscribe(nationObserver));
    }
  }

  @OnClick(R.id.sign_in_button) public void onClickSignIn() {
    googleApiConnectionHandler.connect();
  }

  @Override protected void onStart() {
    super.onStart();
    googleApiConnectionHandler.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
    googleApiConnectionHandler.onStop();
  }

  @Override protected void onActivityResult(int requestCode, int responseCode, Intent data) {
    super.onActivityResult(requestCode, responseCode, data);
    googleApiConnectionHandler.onActivityResult(requestCode, responseCode);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    subscriptions.unsubscribe();
  }

  @OnClick(R.id.fab) public void onClickNext() {
    ProgressDialog progressDialog = buildProgressDialog();
    progressDialog.show();

    okButton.setVisibility(View.GONE);
    Nation nation = (Nation) nationsAdapter.getItem(countriesSpinner.getSelectedItemPosition());
    userPreferences.nationPreference().set(nation);
    userPreferences.coachPreference().set(displayName);
    userPreferences.coinsPreference().set(UserPreferences.INITIAL_COINS_AMOUNT);

    subscriptions.add(initializer
        .initialize(nation.id(), progressDialog)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletionObserver<Object>(this, TAG, "Failed to load game data.") {
          @Override public void onCompleted() {
            progressDialog.setProgress(100);
            progressDialog.dismiss();
            launchHomeScreen();
          }
        }));
  }

  private ProgressDialog buildProgressDialog() {
    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMax(100);
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setTitle(R.string.loading);
    progressDialog.setMessage(getString(R.string.please_wait_downloading_data));
    return progressDialog;
  }

  private void launchHomeScreen() {
    Club club = userPreferences.club();
    startActivity(CurrentTeamDetailsActivity.newIntent(MainActivity.this, club));
    finish();
  }

  public static Intent newIntent(Context context) {
    return new Intent(context, MainActivity.class);
  }
}
