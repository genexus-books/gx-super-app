//
//  SampleExObjHandlerBridge.m
//  ExampleSuperApp
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <GXCoreBL/GXCoreBL.h>

@interface RCT_EXTERN_MODULE(SampleExObjHandler, GXActionExternalObjectHandler)

RCT_EXTERN_METHOD(gxActionExObjMethodHandler_PayWithUI:(NSArray *))
RCT_EXTERN_METHOD(gxActionExObjMethodHandler_PayWithoutUI:(NSArray *))
RCT_EXTERN_METHOD(gxActionExObjMethodHandler_GetClientInformation:(NSArray *))
RCT_EXTERN_METHOD(gxActionExObjMethodHandler_GetPaymentInformation:(NSArray *))
RCT_EXTERN_METHOD(gxActionExObjMethodHandler_GetPaymentInfoAffinity)

+ (BOOL)requiresMainQueueSetup;
{
  return NO;
}

@end
