# EliFUT Android app

[![Build Status](https://travis-ci.org/EliFUT/android.svg)](https://travis-ci.org/EliFUT/android)

## Download the app

[![Google Play](https://play.google.com/intl/en_us/badges/images/badge_new.png)](https://play.google.com/store/apps/details?id=com.elifut)

## Demo

![demo](https://raw.githubusercontent.com/EliFUT/android/master/screenshots/demo.gif)

You can also watch the [Youtube video](https://www.youtube.com/watch?v=_aqgQbdzd0U).

## Introduction

This is an Android open source "football manager style" game inspired on the classic "old but gold"
Windows game [Elifoot 98](elifoot.net) based on the FIFA 15 Ultimate Team data.
Check out the original [Medium post](https://medium.com/@felipecsl/creating-an-android-app-for-beginners-part-i-410a7a64d9b1)
for a more detailed introduction and motivation.

Besides being a fun soccer manager game, this app also aims to follow the best practices
on Android development and be some kind of reference implementation, both in terms of engineering
(code) and design (follow platform UI/UX standards). We're trying to showcase and promote here
some core concepts of Android development and advanced usage of some libraries.

## Technical details

Below is a non-comprehensive list of the libraries/plugins/patterns used:

 * Dependency Injection using [Dagger](https://github.com/google/dagger)
 * Image caching and loading using [Picasso](https://github.com/square/picasso)
 * Unit testing using JUnit and [Robolectric](https://github.com/robolectric/robolectric)
 * Reactive programming using [RxJava](https://github.com/reactivex/rxjava) and [RxAndroid](https://github.com/reactivex/rxandroid)
 * `View` binding using [ButterKnife](https://github.com/jakewharton/butterknife)
 * JSON manipulation using [Moshi](https://github.com/square/moshi)
 * Value classes using [AutoValue](https://github.com/google/auto)
 * Networking using [OkHttp](https://github.com/square/okhttp)
 * HTTP client using [Retrofit](https://github.com/square/retrofit)
 * `Activity`/`Fragment` state management using [Icepick](https://github.com/frankiesardo/icepick)
 * Reactive `SharedPreferences` using [rx-preferences](https://github.com/f2prateek/rx-preferences)
 * Reactive SQLite data using [SqlBrite](https://github.com/square/sqlbrite)
 * Code-generation based `Parcelable` implementation using [auto-value-parcel](https://github.com/rharter/auto-value-parcel)
 * Code-generation based Moshi JSON adapters using [auto-value-moshi](https://github.com/rharter/auto-value-moshi)
 * Code-generation based Cursor adapters using [auto-value-cursor](https://github.com/gabrielittner/auto-value-cursor)
 * Additional Java utility classes provided by [Guava](https://github.com/google/guava)
 * Debugging using [Stetho](https://github.com/facebook/stetho)
 * Material design widgets using AppCompat and the Support Design Library

## Building and running

Run `./gradlew check` to compile and run the unit tests. If everything passes, you can run the
app on any emulator or device with API > 21 with `./gradlew assembleDebug installDebug`.
By default, the app will be pointing to the production API. You can change that to use your local
development API by setting the `API_ENPOINT` constant in the build.gradle to `$devApiEndpoint`:
`buildConfigField "String", "API_ENDPOINT", "\"$devApiEndpoint\""`.

[Generate](https://developers.google.com/analytics/devguides/collection/android/v4) a
`google-services.json` file for Google Analytics and replace the placeholder in the `app/src/release` directory.

In order to build a signed release app, you'll also need to define a few environment variables:
* `ELIFUT_CERT_PASSWORD`: path to your certificate password
* `ELIFUT_FABRIC_API_KEY`: fabric.io API key

Create an Organization and App on [Fabric dashboard](https://fabric.io) and create a string `fabric_api_key`
with your Fabric API key

## Contributing

 * Check out the latest master to make sure the feature hasn't been implemented or the bug hasn't been fixed yet
 * Check out the issue tracker to make sure someone already hasn't requested it and/or contributed it
 * Fork and `git clone` the project
 * Start a feature/bugfix branch, eg.: `yourname/fix-bug-xyz` or `yourname/implement-feature-abc`
 * Commit and push until you are happy with your contribution
 * Make sure to add tests for it. This is important so I don't break it in a future version unintentionally.
 * Open a Pull Request and describe your motivation and changes. Make sure the Travis build passed
 on your branch by making sure it has a green checkmark
 * Squash the commits so we can keep a clean history

## Acknowledgements

**Huge thanks to Square and their Android team for releasing such a huge
number of very high quality libraries. As you can see from the list above, the vast majority of
them are created and maintained by Square.**

## License

```
Copyright 2015 Felipe Lima

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
