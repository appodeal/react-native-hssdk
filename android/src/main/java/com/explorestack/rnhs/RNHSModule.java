package com.explorestack.rnhs;

import android.util.Log;

import com.explorestack.hs.sdk.HSApp;
import com.explorestack.hs.sdk.HSAppConfig;
import com.explorestack.hs.sdk.HSAppInitializeListener;
import com.explorestack.hs.sdk.HSError;
import com.explorestack.hs.sdk.HSInAppPurchase;
import com.explorestack.hs.sdk.HSInAppPurchaseValidateListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;


public class RNHSModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNHSModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;
    }

    private static final String E_INVALID_CONFIG = "E_INVALID_CONFIG";
    private static final String E_NATIVE_SDK_FAILED = "E_NATIVE_SDK_FAILED";
    private static final String E_VALIDATION_FAILED = "E_VALIDATION_FAILED";

    @Override
    public String getName() {
        return "RNHolisticSolution";
    }

    @ReactMethod
    public void configure(ReadableMap configurationData, Promise promise) {
        try {
            HSAppConfig config = new RNHSConfig(configurationData).getAppConfig();
            HSApp.initialize(reactContext, config, new HSAppInitializeListener() {
                @Override
                public void onAppInitialized(@androidx.annotation.Nullable List<HSError> list) {
                    if (list != null && list.get(0) != null) {
                        Exception exc = new Exception(list.get(0).toString());
                        promise.reject(E_NATIVE_SDK_FAILED, exc);
                    } else {
                        promise.resolve(null);
                    }
                }
            });
        } catch (Exception exc) {
            promise.reject(E_INVALID_CONFIG, exc);
        }
    }

    @ReactMethod void validateAndTrackInAppPurchase(ReadableMap purchaseData, Promise promise) {
        HSInAppPurchase purchase = HSInAppPurchase.newBuilder()
                .withPublicKey(purchaseData.getString("publicKey"))
                .withSignature(purchaseData.getString("signature"))
                .withPurchaseData(purchaseData.getString("purchaseData"))
                .withPrice(purchaseData.getString("price"))
                .withCurrency(purchaseData.getString("currency"))
                .withAdditionalParams(RNHSUtils.stringMapFromReadableMap(purchaseData.getMap("additionalParameters")))
                .build();
        HSApp.validateInAppPurchase(purchase, new HSInAppPurchaseValidateListener() {
            @Override
            public void onInAppPurchaseValidateSuccess(@NonNull HSInAppPurchase hsInAppPurchase, @androidx.annotation.Nullable List<HSError> list) {
                Map<String, Object> response = Collections.emptyMap();
                response.put("publicKey", hsInAppPurchase.getPublicKey());
                response.put("signature", hsInAppPurchase.getPublicKey());
                response.put("purchaseData", hsInAppPurchase.getPurchaseData());
                response.put("price", hsInAppPurchase.getPrice());
                response.put("currency", hsInAppPurchase.getCurrency());
                response.put("additionalParameters", hsInAppPurchase.getAdditionalParameters());
                promise.resolve(response);
            }

            @Override
            public void onInAppPurchaseValidateFail(@NonNull List<HSError> list) {
                Exception exc = new Exception(list.get(0).toString());
                promise.reject(E_VALIDATION_FAILED, exc);
            }
        });
    }

    @ReactMethod void trackEvent(String name, @Nullable ReadableMap parameters) {
        if (parameters == null) {
            HSApp.logEvent(name);
        } else {
            Map<String, Object> parametersMap = RNHSUtils.mapFromReadableMap(parameters);
            HSApp.logEvent(name, parametersMap);
        }
    }
}