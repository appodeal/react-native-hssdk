#import "RNHolisticSolution.h"
#import "RNHSConfiguration.h"
#import <HolisticSolutionSDK/HolisticSolutionSDK-Swift.h>
#import <React/RCTUtils.h>


@interface RNHolisticSolution ()

@end

@implementation RNHolisticSolution

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

#pragma mark - Method export

RCT_EXPORT_METHOD(configure:(NSDictionary *)configuration
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    RNHSConfiguration *config = [[RNHSConfiguration alloc] initWithConfig:configuration];
    [HSApp configureWithConfiguration:config.configuration completion:^(NSError *error) {
        if (error) {
            reject ? reject(@"HSSDK", @"Failed to initialise", error) : nil;
        } else {
            resolve ? resolve(nil) : nil;
        }
    }];
}


RCT_EXPORT_METHOD(validateAndTrackInAppPurchase:(NSDictionary *)purchase
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [HSApp validateAndTrackInAppPurchaseWithProductId:purchase[@"productId"]
                                                price:purchase[@"price"]
                                             currency:purchase[@"currency"]
                                        transactionId:purchase[@"transactionId"]
                                 additionalParameters:purchase[@"additionalParameters"]
                                              success:^(NSDictionary *response) {
        resolve ? resolve(response) : nil;
    }
                                              failure:^(NSError *error, id response) {
        reject ? reject(@"Validation error", error.localizedDescription, error) : nil;
    }];
}

RCT_EXPORT_METHOD(trackEvent:(NSString *)name
                  customParameters:(NSDictionary *)customParameters
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [HSApp trackEvent:name customParameters:customParameters];
    resolve ? resolve(nil) : nil;
}

@end
