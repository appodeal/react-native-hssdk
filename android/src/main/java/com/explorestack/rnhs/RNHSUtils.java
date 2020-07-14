package com.explorestack.rnhs;


import com.appodeal.ads.Appodeal;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RNHSUtils {
    static @Nullable  Map<String, Object> mapFromReadableMap(@Nullable ReadableMap readableMap) {
        Map<String, Object> map = null;
        if (readableMap != null) {
            map = new HashMap<String, Object>();
            ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                ReadableType type = readableMap.getType(key);
                switch (type) {
                    case Boolean:
                        map.put(key, readableMap.getBoolean(key));
                        break;
                    case Number:
                        map.put(key, readableMap.getDouble(key));
                        break;
                    case String:
                        map.put(key, readableMap.getString(key));
                        break;
                }
            }
        }
        return map;
    }

    static @Nullable
    Map<String, String> stringMapFromReadableMap(@Nullable ReadableMap readableMap) {
        Map<String, String> map = null;
        if (readableMap != null) {
            map = new HashMap<String, String>();
            ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                String value = readableMap.getString(key);
                if (value != null) {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    static @Nullable
    List<String> listFromReadableArray(@Nullable ReadableArray readableArray) {
        List<String> list = null;
        if (readableArray != null) {
            list = new ArrayList<>();
            for (int idx = 0; idx < readableArray.size(); idx ++) {
                String value = readableArray.getString(idx);
                if (value != null) {
                    list.add(value);
                }
            }
        }
        return list;
    }
}