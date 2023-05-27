import { NativeModules } from 'react-native';

export class MethodCall {
  constructor(public method: string, public args: any) {}
}

export class MethodChannel {
  private channelName: string;
  private methodCallHandler: ((call: MethodCall) => Promise<void>) | null = null;
  private RCTMethodChannelModule  = NativeModules;

  constructor(channelName: string) {
    this.channelName = channelName;
    this.registerMethodChannel();
  }

  public registerMethodChannel() {
    this.RCTMethodChannelModule.registerMethodChannel(
      this.channelName,
      (method: string, args: any) => this.handleMethodCall(method, args)
    );
  }

  private handleMethodCall = (method: string, args: any) => {
    const call = new MethodCall(method, args);
    if (this.methodCallHandler) {
      return this.methodCallHandler(call);
    }
    throw new Error(`No method call handler registered for channel '${this.channelName}'`);
  };

  public setMethodCallHandler(handler: (call: MethodCall) => Promise<void>) {
    this.methodCallHandler = handler;
  }

  public invokeMethod(method: string, args: any): Promise<any> {
    return NativeModules.YourNativeModule.invokeMethod(this.channelName, method, args);
  }
}

export default MethodChannel;
