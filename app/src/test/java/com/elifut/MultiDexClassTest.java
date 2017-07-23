package com.elifut;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;

public class MultiDexClassTest {
  @Test public void constructorShouldBePrivate()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MultiDex> constructor = MultiDex.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}
