//
//  ProvisioningAPIBridge.m
//  ExampleSuperApp
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ProvisioningAPI, NSObject)

RCT_EXTERN_METHOD(handleMethodCall:(NSDictionary *)call result:(RCTResponseSenderBlock)result)

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

@end
