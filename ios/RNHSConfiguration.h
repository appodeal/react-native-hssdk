//
//  RNHSConfiguration.h
//  DoubleConversion
//
//  Created by Stas Kochkin on 07.07.2020.
//

#import <Foundation/Foundation.h>
#import <HolisticSolutionSDK/HolisticSolutionSDK-Swift.h>


NS_ASSUME_NONNULL_BEGIN

@interface RNHSConfiguration : NSObject

@property (nonatomic, readonly) HSAppConfiguration *configuration;

- (instancetype)initWithConfig:(NSDictionary <NSString *, id> *)config;

@end

NS_ASSUME_NONNULL_END
