package com.felipecsl.elifut;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

public class ElifutTestRunner extends RobolectricGradleTestRunner {
  public static final String MANIFEST_PATH = "../app/src/main/AndroidManifest.xml";

  public ElifutTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected AndroidManifest getAppManifest(Config config) {
    String res = String.format("../app/build/intermediates/res/merged/%1$s/%2$s",
        BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
    String asset = "src/main/assets";
    return new AndroidManifest(Fs.fileFromPath(MANIFEST_PATH), Fs.fileFromPath(res),
        Fs.fileFromPath(asset));
  }
}
