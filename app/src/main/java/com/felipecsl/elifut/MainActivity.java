package com.felipecsl.elifut;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.felipecsl.elifut.adapter.CountriesSpinnerAdapter;
import com.felipecsl.elifut.models.Nation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.input_name) EditText inputName;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.countries_spinner) Spinner countriesSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    collapsingToolbar.setTitle(getTitle());

    ElifutApplication application = (ElifutApplication) getApplication();
    application.service()
        .nations()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<List<Nation>>>() {
          @Override public void onCompleted() {
          }

          @Override public void onError(Throwable throwable) {
            Toast.makeText(MainActivity.this, "Failed to load list of countries",
                Toast.LENGTH_SHORT).show();
            Log.w(TAG, throwable);
          }

          @Override public void onNext(Response<List<Nation>> nations) {
            CountriesSpinnerAdapter nationsAdapter = new CountriesSpinnerAdapter(
                MainActivity.this, nations.body());
            countriesSpinner.setAdapter(nationsAdapter);
          }
        });
  }
}
