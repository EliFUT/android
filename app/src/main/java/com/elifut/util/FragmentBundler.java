package com.elifut.util;

import android.support.v4.app.Fragment;

/**
 * Set Fragment arguments like a Pro.
 *
 * @param <F> The mFragment setup.
 */
public class FragmentBundler<F extends Fragment> {

  private final F mFragment;
  private final FragmentBundleBuilder<F> mBundle;

  private FragmentBundler(F fragment) {
    mFragment = fragment;
    mBundle = new FragmentBundleBuilder<>(this);
  }

  public static <F extends Fragment> FragmentBundleBuilder<F> make(F fragment) {
    return new FragmentBundler<>(fragment).bundle();
  }

  public F build() {
    mFragment.setArguments(mBundle.toBundle());
    return mFragment;
  }

  public FragmentBundleBuilder<F> bundle() {
    return mBundle;
  }
}
