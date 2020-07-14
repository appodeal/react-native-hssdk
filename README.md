# react-native-hssdk

React Native package that adds Holistic Solution SDK support to your react-native application.

## Table of Contents

* [Installation](#installation)
* [Usage](#usage)
  + [Initialisation](#initialisation)
  + [Events](#tracking)
  + [Purchases](#purchases)
* [Changelog](#changelog)

## Installation

Run following commands in project root directory

``` bash
yarn add react-native-hssdk
```

#### iOS

1. Go to `ios` folder and run

``` bash
pod install
```

If you use `use_frameworks!` you can be faced with the problem of CocoaPods installation such this:

``` bash
[!] The 'Pods-{TARGET}' target has transitive dependencies that include statically linked binaries: (FirebaseCore, FirebaseCoreDiagnostics, GoogleDataTransportCCTSupport, GoogleDataTransport, FirebaseInstallations, FirebaseRemoteConfig, and FirebaseABTesting)
```

You could add `pre_install` hook to the bottom of the project's Podfile:

``` ruby
pre_install do |installer|
  installer.pod_targets.each do |pod|
    if pod.name.start_with?('RNFB')
      def pod.build_type;
        Pod::BuildType.static_library
      end
    end
  end
end
```

2. Add all required data that is necessary for services work:

- [Firebase docs](https://firebase.google.com/docs/ios/setup#add-config-file)
- [Facebook docs](https://developers.facebook.com/docs/app-events/getting-started-app-events-ios)

#### Android

1. Add all required data that is necessary for services work:

- [Firebase docs](https://firebase.google.com/docs/android/setup#add-config-file)
- [Facebook docs](https://firebase.google.com/docs/android/setup#add-config-file)

2. Resolve **AndroidManifest.xml** conflicts

``` xml
<manifest 
  xmlns:android="http://schemas.android.com/apk/res/android"
  ...
  xmlns:tools="http://schemas.android.com/tools"
>
  ...
  <application
    ...
    android:allowBackup="false"
    android:fullBackupContent="false"
    tools:replace="android:fullBackupContent,android:allowBackup"
  >
```

## Usage

Holistic Solution will initialise all services: **Facebook Analytics**, **Firebase Remote Config**, **AppsFlyer**.
And sync theirs data with Appodeal. After all services is initialised Holistic Solution will resolve.

### Initialisation

At the application start moment you need to call `configure` method. After this promise is resolved you could initialise Appodeal SDK.

> Note. To maximise your ad revenue, initialise Appodeal SDK even in `catch` function.

``` js

import { HolisticSolution } from 'react-native-hssdk';

const hsconfig = {
    timeout: 30,
    appsFlyer: {
        devKey: "YOUR APPSFLYER DEV KEY",
        appId: "YOUR APPSFLYER APP ID (iOS)",
    }, 
    firebase: {
        defaults: {
            "feature_1": "enabled",
            "feature_2": "disabled"
        },
    }
}

export const App = () => {
    useEffect(() => {
        HolisticSolution.configure(hsconfig)
        .then(() => {
          console.log("Holistic solution has been configured");
          // Initialise Appodeal here
        })
        .catch(error => {
            console.log("Holistic solution error: " + error)
            // Initialise Appodeal here
        });
    }, []);
```

### Events

Holistic Solution can track events in all services.

``` js
// Name only
HolisticSolution.trackEvent("game_started");
// Name and parameters
HolisticSolution.trackEvent("game_finished", {"difficulty": "medium"})
```

### Purchases

Holistic soltution can validate and track in-app purchases by AppsFlyer service

``` js

const iOSPurchase = {
        price: "0.99",
        productId: "unique product",
        transactionId: "XXX",
        currency: "USD",
        additionalParameters: {}  
}

    const AndroidPurchase = {
        price: "0.99",
        currency: "USD",
        publicKey: "unique key",
        signature: "signature",
        purchaseData: "purchase data",
        additionalParameters: {}  
    }

HolisticSolution.validateAndTrackInAppPurchase(Platform.OS === 'ios' ? iOSPurchase : AndroidPurchase)
  .then(response => console.log("Purchase is valid: " + response))
  .catch(error => console.log("Purchase is invalid: " + error));
```

## Changelog
