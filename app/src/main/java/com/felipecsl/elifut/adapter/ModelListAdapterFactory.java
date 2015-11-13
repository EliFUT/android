package com.felipecsl.elifut.adapter;

import com.felipecsl.elifut.models.Model;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public final class ModelListAdapterFactory<T extends Model> implements JsonAdapter.Factory {
  private final Class<T> type;

  public ModelListAdapterFactory(Class<T> type) {
    this.type = type;
  }

  @Override
  public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (type.equals(Types.newParameterizedType(List.class, this.type))) {
      return new ModelListAdapter<>(this.type, moshi);
    }
    return null;
  }
}
