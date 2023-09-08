//
//  GXUIApplicationHelper.h
//  ExampleSuperApp
//
//  Created by Cinthya Cristina Riveros on 14/6/23.
//

#ifndef GXUIApplicationHelper_h
#define GXUIApplicationHelper_h

#import <Foundation/Foundation.h>

@interface GXUIApplicationHelper : NSObject

+ (nonnull instancetype)shared;

- (void)initializeGX;

@end

#endif /* GXUIApplicationHelper_h */
