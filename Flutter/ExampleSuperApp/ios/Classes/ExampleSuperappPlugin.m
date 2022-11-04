#import "ExampleSuperappPlugin.h"
#if __has_include(<example_superapp/example_superapp-Swift.h>)
#import <example_superapp/example_superapp-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "example_superapp-Swift.h"
#endif

@implementation ExampleSuperappPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftExampleSuperappPlugin registerWithRegistrar:registrar];
}
@end
