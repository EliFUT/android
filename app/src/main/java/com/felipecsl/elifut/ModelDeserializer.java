package com.felipecsl.elifut;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.felipecsl.elifut.models.Model;

import java.io.IOException;

final class ModelDeserializer extends StdDeserializer<Model> {

  protected ModelDeserializer() {
    super(Model.class);
  }

  @Override public Model deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException {
    return null;
  }
}
