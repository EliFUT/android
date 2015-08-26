package com.felipecsl.elifut;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.felipecsl.elifut.models.Model;

final class ServiceModule extends SimpleModule {
  public ServiceModule() {
    addDeserializer(Model.class, new ModelDeserializer());
  }
}
