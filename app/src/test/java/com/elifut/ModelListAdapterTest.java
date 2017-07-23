package com.elifut;

import com.elifut.adapter.ModelListAdapterFactory;
import com.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okio.Buffer;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelListAdapterTest {
  private final Moshi moshi = new Moshi.Builder()
      .add(new ModelListAdapterFactory<>(Nation.class))
      .add(AutoValueMoshiAdapterFactory.create())
      .build();

  @Test public void testModelListAdapter() throws IOException {
    Buffer buffer = new Buffer();
    JsonWriter jsonWriter = JsonWriter.of(buffer);
    JsonAdapter<List<Nation>> adapter =
        moshi.adapter(Types.newParameterizedType(List.class, Nation.class));
    Nation nation = Nation.create(1, 1, "foo", "bar", "baz");
    List<Nation> nations = Collections.singletonList(nation);
    adapter.toJson(jsonWriter, nations);
    String json = buffer.readUtf8();
    buffer = new Buffer().writeUtf8(json);
    JsonReader jsonReader = JsonReader.of(buffer);
    assertThat(adapter.fromJson(jsonReader)).isEqualTo(nations);
  }
}
