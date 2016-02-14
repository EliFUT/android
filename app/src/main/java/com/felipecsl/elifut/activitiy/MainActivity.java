package com.felipecsl.elifut.activitiy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.felipecsl.elifut.AppInitializer;
import com.felipecsl.elifut.CompletionObserver;
import com.felipecsl.elifut.R;
import com.felipecsl.elifut.ResponseObserver;
import com.felipecsl.elifut.adapter.CountriesSpinnerAdapter;
import com.felipecsl.elifut.models.Club;
import com.felipecsl.elifut.models.Nation;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends ElifutActivity {
  private static final String TAG = "MainActivity";

  @Inject AppInitializer initializer;

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.input_name) EditText inputName;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.countries_spinner) Spinner countriesSpinner;
  @Bind(R.id.fab) FloatingActionButton okButton;

  private CountriesSpinnerAdapter nationsAdapter;
  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private final Observer<List<Nation>> nationObserver =
      new ResponseObserver<List<Nation>>(this, TAG, "Failed to load list of countries.") {
        @Override public void onNext(List<Nation> response) {
          nationsAdapter = new CountriesSpinnerAdapter(MainActivity.this, response);
          countriesSpinner.setAdapter(nationsAdapter);
        }
      };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    daggerComponent().inject(this);
    setSupportActionBar(toolbar);

    Nation nation = userPreferences.nationPreference().get();

    if (nation != null) {
      launchHomeScreen();
    } else {
      subscriptions.add(service.nations()
          .compose(this.<List<Nation>>applyTransformations())
          .subscribe(nationObserver));
    }
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
    userPreferences.coachPreference().set(inputName.getText().toString());

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
