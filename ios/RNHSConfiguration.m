//
//  RNHSConfiguration.m
//  DoubleConversion
//
//  Created by Stas Kochkin on 07.07.2020.
//

#import "RNHSConfiguration.h"
#import <React/RCTUtils.h>


@interface RNHSConfiguration ()

@property (nonatomic, assign) NSTimeInterval timeout;
@property (nonatomic, strong) NSArray <id<HSService>> *services;

@end

@implementation RNHSConfiguration

- (instancetype)initWithConfig:(NSDictionary<NSString *,id> *)config {
    if (self = [super init]) {
        [self setupWithConfig:config];
    }
    return self;
}

- (HSAppConfiguration *)configuration {
    HSAppodealConnector *appodeal = [HSAppodealConnector new];
    HSAppConfiguration *configuration = [[HSAppConfiguration alloc] initWithServices:self.services
                                                                          connectors:@[appodeal]
                                                                             timeout:self.timeout
                                                                               debug:DebugDisabled];
    return configuration;
}

- (void)setupWithConfig:(NSDictionary<NSString *,id> *)config {
    self.timeout = [config[@"timeout"] doubleValue] ?: 30;
    
    HSAppsFlyerConnector *appsflyer;
    {
        NSString *appId = config[@"appsFlyer"][@"appId"];
        NSString *devKey = config[@"appsFlyer"][@"devKey"];
        NSArray <NSString *> *keys = config[@"appsFlyer"][@"keys"];
        
        RCTAssert(appId != nil, @"AppsFlyer app id can't be nil");
        RCTAssert(devKey != nil, @"AppsFlyer dev id can't be nil");
        
        if (![keys isKindOfClass:NSArray.class]) {
            keys = @[];
        }
        
        appsflyer = [[HSAppsFlyerConnector alloc] initWithDevKey:devKey
                                                           appId:appId
                                                            keys:keys];
    }
    
    HSFirebaseConnector *firebase;
    {
        NSTimeInterval expirationDuration = [config[@"firebase"][@"expirationDuration"] doubleValue] ?: 60;
        NSArray <NSString *> *keys = config[@"firebase"][@"keys"];
        NSDictionary *defaults = config[@"firebase"][@"defaults"];
        
        if (![keys isKindOfClass:NSArray.class]) {
            keys = @[];
        }
        if (![defaults isKindOfClass:NSDictionary.class]) {
            defaults = @{};
        }
       
        firebase = [[HSFirebaseConnector alloc] initWithKeys:keys
                                                    defaults:defaults
                                          expirationDuration:expirationDuration];
    }
    
    HSFacebookConnector *facebook = [[HSFacebookConnector alloc] init];
    self.services = @[
        appsflyer,
        firebase,
        facebook
    ];
}

@end
