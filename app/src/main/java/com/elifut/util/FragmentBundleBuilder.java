package com.elifut.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * A bundle builder for fragment arguments.
 *
 * @param <F> The fragment type you're bundling to.
 */
@SuppressWarnings("unchecked")
public class FragmentBundleBuilder<F extends Fragment> extends BundleBuilder {

  private final FragmentBundler<F> mFragmentBundler;

  public FragmentBundleBuilder(FragmentBundler<F> fragmentBundler) {
    mFragmentBundler = fragmentBundler;
  }

  @Override
  public FragmentBundleBuilder<F> putAll(Bundle bundle) {
    return (FragmentBundleBuilder<F>) super.putAll(bundle);
  }

  @Override
  public FragmentBundleBuilder<F> putBoolean(String key, boolean value) {
    return (FragmentBundleBuilder<F>) super.putBoolean(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putInt(String key, int value) {
    return (FragmentBundleBuilder<F>) super.putInt(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putParcelable(String key, Parcelable value) {
    return (FragmentBundleBuilder<F>) super.putParcelable(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putLong(String key, long value) {
    return (FragmentBundleBuilder<F>) super.putLong(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putString(String key, String value) {
    return (FragmentBundleBuilder<F>) super.putString(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putFloat(String key, float value) {
    return (FragmentBundleBuilder<F>) super.putFloat(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putDouble(String key, double value) {
    return (FragmentBundleBuilder<F>) super.putDouble(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putByte(String key, byte value) {
    return (FragmentBundleBuilder<F>) super.putByte(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putChar(String key, char value) {
    return (FragmentBundleBuilder<F>) super.putChar(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putShort(String key, short value) {
    return (FragmentBundleBuilder<F>) super.putShort(key, value);
  }

  @Override
  public FragmentBundleBuilder<F> putSerializable(String key, Serializable value) {
    return (FragmentBundleBuilder<F>) super.putSerializable(key, value);
  }

  public F build() {
    return mFragmentBundler.build();
  }
}
