package com.felipecsl.elifut.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.felipecsl.elifut.R;
import com.felipecsl.elifut.models.Nation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public final class CountriesSpinnerAdapter extends BaseAdapter {
  private final Context context;
  private final List<Nation> countries;
  private final LayoutInflater layoutInflater;

  public CountriesSpinnerAdapter(Context context, List<Nation> countries) {
    this.context = context;
    this.countries = countries;
    layoutInflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {
    return countries.size();
  }

  @Override public Object getItem(int position) {
    return countries.get(position);
  }

  @Override public long getItemId(int position) {
    return ((Nation) getItem(position)).id();
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final TextView v;
    if (convertView == null) {
      v = (TextView) layoutInflater.inflate(R.layout.layout_spinner_country, parent,
          false);
    } else {
      v = (TextView) convertView;
    }

    Nation country = (Nation) getItem(position);
    v.setText(country.toString());

    Picasso.with(context).load(country.image()).into(new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        v.setCompoundDrawablesWithIntrinsicBounds(
            new BitmapDrawable(context.getResources(), bitmap), null, null, null);
      }

      @Override public void onBitmapFailed(Drawable errorDrawable) {
      }

      @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    });

    return v;
  }
}
