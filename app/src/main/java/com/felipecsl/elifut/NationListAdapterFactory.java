package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

final class NationListAdapterFactory implements JsonAdapter.Factory {
  @Override
  public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (type.equals(Types.newParameterizedType(List.class, Nation.class))) {
      return new NationsAdapter(moshi);
    }
    return null;
  }
}
