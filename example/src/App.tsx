import React, { useEffect } from 'react';
import { StyleSheet, StatusBar, SafeAreaView, Button, View, Platform } from 'react-native';
import { HolisticSolution } from 'react-native-hssdk';
import { Appodeal, AppodealAdType, AppodealLogLevel } from 'react-native-appodeal';

const hsconfig = {
    timeout: 30,
    appsFlyer: {
        devKey: "ewVfXy4eavTcRaRzrsKWAA",
        appId: "1490003699",
        keys: ["campaign", "adset"]
    }, 
    firebase: {
        expirationDuration: 60,
        keys: ["button_color"],
        defaults: {
            "button_color": "red",
            "large_headers": "disabled"
        },
    }
}

const Separator = () => (
    <View style={styles.separator} />
);

const initialiseAppodeal = () => {
    console.log("Initialise Appodeal");
    Appodeal.initialize("Appodeal App Key", AppodealAdType.BANNER, true);
    Appodeal.show(AppodealAdType.BANNER_BOTTOM);
}

export const App = () => {
    useEffect(() => {
        Appodeal.setLogLevel(AppodealLogLevel.VERBOSE);
        HolisticSolution.configure(hsconfig)
        .then(() => initialiseAppodeal())
        .catch(error => {
            console.log("Holistic solution error: " + error)
            initialiseAppodeal();
        });
    }, []);

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

    return (
        <>
            <StatusBar />
            <SafeAreaView style={styles.container}>
                <Button title="Synthesize purchase" onPress={() => {
                    HolisticSolution.validateAndTrackInAppPurchase(Platform.OS === 'ios' ? iOSPurchase : AndroidPurchase)
                    .then(response => console.log("Purchase is valid: " + response))
                    .catch(error => console.log("Purchase is invalid: " + error));
                }}/>
                <Separator/>
                <Button title="Synthesize event" onPress={() => {
                    HolisticSolution.trackEvent("game_started");
                    HolisticSolution.trackEvent("game_finished", {"difficulty": "medium"})
                }}/>
            </SafeAreaView>
        </>
    );
}

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    scrollView: {
        flex: 1,
        alignSelf: 'stretch',
        backgroundColor: 'hsl(0, 0%, 97%)',
    },
    separator: {
        marginVertical: 8,
    }
});