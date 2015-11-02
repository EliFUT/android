package com.felipecsl.elifut;

import com.felipecsl.elifut.models.Model;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class ModelListAdapter<T extends Model> extends JsonAdapter<List<T>> {
  private final Class<T> type;
  private final Moshi moshi;

  public ModelListAdapter(Class<T> type, Moshi moshi) {
    this.type = type;
    this.moshi = moshi;
  }

  @Override public List<T> fromJson(JsonReader reader) throws IOException {
    List<T> nations = new ArrayList<>();
    reader.beginObject();
    reader.nextName();
    reader.beginArray();
    while (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
      nations.add(moshi.adapter(type).fromJson(reader));
    }
    reader.endArray();
    reader.endObject();
    return nations;
  }

  @Override public void toJson(JsonWriter writer, List<T> value) throws IOException {
    writer.beginObject();
    writer.name(type.getSimpleName().toLowerCase());
    writer.beginArray();
    JsonAdapter<T> adapter = moshi.adapter(type);
    for (T n : value) {
      adapter.toJson(writer, n);
    }
    writer.endArray();
    writer.endObject();
  }
}
