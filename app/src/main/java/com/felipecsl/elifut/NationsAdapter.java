package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Nation;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class NationsAdapter extends JsonAdapter<List<Nation>> {
  private final Moshi moshi;

  public NationsAdapter(Moshi moshi) {
    this.moshi = moshi;
  }

  @Override public List<Nation> fromJson(JsonReader reader) throws IOException {
    List<Nation> nations = new ArrayList<>();
    reader.beginObject();
    reader.nextName();
    reader.beginArray();
    while (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
      nations.add(moshi.adapter(Nation.class).fromJson(reader));
    }
    reader.endArray();
    reader.endObject();
    return nations;
  }

  @Override public void toJson(JsonWriter writer, List<Nation> value) throws IOException {
    writer.beginArray();
    JsonAdapter<Nation> adapter = moshi.adapter(Nation.class);
    for (Nation n : value) {
      adapter.toJson(writer, n);
    }
    writer.endArray();
  }
}
