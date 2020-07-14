'use strict';

import { NativeModules } from 'react-native';


const RNHolisticSolution = NativeModules.RNHolisticSolution;


interface HolisticSolutionSDK {
  configure(configuration: RNHolisticSolutionConfiguration): Promise<void>,
  validateAndTrackInAppPurchase(purchase: RNPurchase): Promise<RNParameters>,
  trackEvent(name: String, customParameters?: RNParameters): Promise<void>
}

type RNPurchase = RNPurchaseiOS | RNPurchaseAndroid;

interface RNPurchaseiOS {
  productId: String,
  price: String,
  currency: String,
  transactionId: String,
  additionalParameters: RNParameters,
}

interface RNPurchaseAndroid {
  publicKey: String,
  signature: String,
  purchaseData: String,
  price: String,
  currency: String,
  additionalParameters: RNParameters,
}

interface RNHolisticSolutionAppsFlyer {
  devKey: string,
  appId?: string,
  keys?: string[]
}

interface RNParameters {
  [key: string]: number | string | boolean;
}

interface RNHolisticSolutionFirebase {
  keys?: string[],
  defaults?: RNParameters,
  expirationDuration?: number
}

interface RNHolisticSolutionConfiguration {
  appsFlyer: RNHolisticSolutionAppsFlyer,
  firebase?: RNHolisticSolutionFirebase,   
  timeout?: number
  debug?: boolean
}



const SDK: HolisticSolutionSDK = {
  configure: (configuration: RNHolisticSolutionConfiguration): Promise<void> => {
    return RNHolisticSolution.configure(configuration);
  },
  validateAndTrackInAppPurchase: (purchase: RNPurchase): Promise<RNParameters> => {
    return RNHolisticSolution.validateAndTrackInAppPurchase(purchase);
  },
  trackEvent: (name: String, customParameters?: RNParameters): Promise<void> => {
    return RNHolisticSolution.trackEvent(name, customParameters);
  }
}
 
export default SDK;