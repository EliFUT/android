package com.felipecsl.elifut.util;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * A Bundle that doesn't suck. Allows you to chain method calls as you'd expect.
 */
public class BundleBuilder {

  private final Bundle bundle;

  public BundleBuilder() {
    this(new Bundle());
  }

  public BundleBuilder(Bundle existingBundle) {
    bundle = new Bundle(existingBundle);
  }

  public BundleBuilder putBoolean(String key, boolean value) {
    bundle.putBoolean(key, value);
    return this;
  }

  public BundleBuilder putByte(String key, byte value) {
    bundle.putByte(key, value);
    return this;
  }

  public BundleBuilder putChar(String key, char value) {
    bundle.putChar(key, value);
    return this;
  }

  public BundleBuilder putShort(String key, short value) {
    bundle.putShort(key, value);
    return this;
  }

  public BundleBuilder putInt(String key, int value) {
    bundle.putInt(key, value);
    return this;
  }

  public BundleBuilder putLong(String key, long value) {
    bundle.putLong(key, value);
    return this;
  }

  public BundleBuilder putFloat(String key, float value) {
    bundle.putFloat(key, value);
    return this;
  }

  public BundleBuilder putDouble(String key, double value) {
    bundle.putDouble(key, value);
    return this;
  }

  public BundleBuilder putString(String key, String value) {
    bundle.putString(key, value);
    return this;
  }

  public BundleBuilder putIntArray(String key, int[] value) {
    bundle.putIntArray(key, value);
    return this;
  }

  public BundleBuilder putStringArray(String key, String[] value) {
    bundle.putStringArray(key, value);
    return this;
  }

  public BundleBuilder putParcelable(String key, Parcelable value) {
    bundle.putParcelable(key, value);
    return this;
  }

  public BundleBuilder putAll(Bundle bundle) {
    this.bundle.putAll(bundle);
    return this;
  }

  public BundleBuilder putSerializable(String key, Serializable value) {
    this.bundle.putSerializable(key, value);
    return this;
  }

  /**
   * Converts this BundleBuilder into a plain Bundle object.
   *
   * @return A new Bundle containing all the mappings from the current BundleBuilder.
   */
  public Bundle toBundle() {
    return new Bundle(bundle);
  }
}
