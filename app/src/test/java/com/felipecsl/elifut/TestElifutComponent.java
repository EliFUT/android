package com.felipecsl.elifut;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { TestNetworkModule.class, TestDataModule.class })
public interface TestElifutComponent extends ElifutComponent, TestElifutGraph {

  /** An initializer that creates the graph from an application. */
  final class Initializer {
    static ElifutComponent init(ElifutApplication app) {
      return DaggerTestElifutComponent.builder()
          .testNetworkModule(new TestNetworkModule(app))
          .testDataModule(new TestDataModule(app))
          .build();
    }

    private Initializer() {
    } // No instances.
  }
}
