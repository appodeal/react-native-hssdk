package com.explorestack.rnhs;

import com.appodeal.ads.Appodeal;
import com.explorestack.hs.sdk.HSApp;
import com.explorestack.hs.sdk.HSAppConfig;
import com.explorestack.hs.sdk.connector.appodeal.HSAppodealConnector;
import com.explorestack.hs.sdk.service.appsflyer.HSAppsflyerService;
import com.explorestack.hs.sdk.service.facebook.HSFacebookService;
import com.explorestack.hs.sdk.service.firebase.HSFirebaseService;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


class RNHSConfig {
    private final ReadableMap data;

    public RNHSConfig(ReadableMap data) {
        super();
        this.data = data;
    }

    HSAppConfig getAppConfig() throws Exception {
        HSAppodealConnector appodealConnector = new HSAppodealConnector();
        HSFacebookService facebookService = new HSFacebookService();
        HSFirebaseService firebaseService = getFirebaseService();
        HSAppsflyerService appsflyerService = getAppsFlyerService();


        HSAppConfig appConfig = new HSAppConfig()
                .withConnectors(appodealConnector)
                .withServices(appsflyerService, facebookService, firebaseService);

        if (data.hasKey("debug")) {
            boolean debug = data.getBoolean("debug");
            appConfig.setDebugEnabled(debug);
        }

        if (data.hasKey("timeout")) {
            int timeout = data.getInt("timeout");
            Long timeoutLong = Long.valueOf(timeout);
            appConfig.setComponentInitializeTimeout(timeoutLong);
        }

        return appConfig;
    }

    private HSAppsflyerService getAppsFlyerService() throws Exception {
        ReadableMap appsFlyer = data.getMap("appsFlyer");
        if (appsFlyer == null) {
            throw new Exception("Config doesn't contain AppsFlyer");
        }

        String devKey = appsFlyer.getString("devKey");
        if (devKey == null) {
            throw new Exception("AppsFLyer config doesn't contain devKey");
        }

        List<String> conversionKeys = null;
        if (appsFlyer.hasKey("keys")) {
            ReadableArray keys = appsFlyer.getArray("keys");
            conversionKeys = RNHSUtils.listFromReadableArray(keys);
        }

        HSAppsflyerService appsflyerService = new HSAppsflyerService(devKey, conversionKeys);
        return appsflyerService;
    }

    private HSFirebaseService getFirebaseService() {
        HSFirebaseService service;
        ReadableMap firebase = data.getMap("firebase");
        if (firebase == null) {
            service = new HSFirebaseService();
        } else {
            Map<String, Object> defaultsMap = null;
            if (firebase.hasKey("defaults")) {
                ReadableMap defaults = firebase.getMap("defaults");
                defaultsMap = RNHSUtils.mapFromReadableMap(defaults);
            }

            Long durationLong = null;
            if (firebase.hasKey("expirationDuration")) {
                int duration = firebase.getInt("expirationDuration");
                durationLong = Long.valueOf(duration);
            }

            service = new HSFirebaseService(defaultsMap, durationLong);
        }
        return service;
    }
}
