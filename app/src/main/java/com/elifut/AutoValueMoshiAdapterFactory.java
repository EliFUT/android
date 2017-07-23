package com.elifut;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

@MoshiAdapterFactory
public abstract class AutoValueMoshiAdapterFactory implements JsonAdapter.Factory {
  // Static factory method to access the package private generated implementation
  public static JsonAdapter.Factory create() {
    return new AutoValueMoshi_AutoValueMoshiAdapterFactory();
  }
}
