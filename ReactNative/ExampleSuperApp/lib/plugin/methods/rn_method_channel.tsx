import React from 'react';

import SuperAppApiInterface from './rn_method_handler_interface';
import MethodChannel, { MethodCall } from './method_channel';
import { MainScreenStateContext } from '../../../App';

type MethodChannelListener = (call: MethodCall) => Promise<void>;

interface RNMethodChannelProps {
  buildContext: React.Context<MainScreenStateContext| null>; // Update the type of buildContext
}

class RNMethodChannel extends React.Component<RNMethodChannelProps> {
  static configureChannel() {
    throw new Error('Method not implemented.');
  }
  static channelName = 'example_superapp';
  static methodPayNoUi = 'PayWithoutUI';
  static methodPayWithUi = 'PayWithUI';
  static methodGetClientInformation = 'GetClientInformation';
  static methodGetPaymentInformation = 'GetPaymentInformation';
  static methodGetPaymentInfoAffinity = 'GetPaymentInfoAffinity';

  context: React.ContextType<any>;
  api: SuperAppApiInterface = SuperAppApiInterface.instance;
  channel: MethodChannel | undefined;

  constructor(props: RNMethodChannelProps) {
    super(props);
    this.context = props.buildContext;
    this.channel = new MethodChannel(RNMethodChannel.channelName);
    this.channel.setMethodCallHandler(this._methodHandler);
    // NativeModules.YourNativeModule.registerMethodHandler(this._methodHandler);
  }

  _methodHandler: MethodChannelListener = async (call: MethodCall) => {
    let result = null; // Default value for the result
    switch (call.method) {
      case RNMethodChannel.methodPayNoUi:
        const payNoUiAmount = call.args.amount;
        result = this.api.payNoUi(payNoUiAmount);
        break;
      case RNMethodChannel.methodPayWithUi:
        const payWithUiAmount = call.args.amount;
        result = await this.api.payWithUi(payWithUiAmount, this.context);
        break;
      case RNMethodChannel.methodGetClientInformation:
        const clientId = call.args.clientId;
        result = this.api.getClientInformation(clientId);
        break;
      case RNMethodChannel.methodGetPaymentInformation:
        const clientInfo = call.args.clientInfo;
        result = this.api.getPaymentInformation(clientInfo);
        break;
      case RNMethodChannel.methodGetPaymentInfoAffinity:
        const paymentInfo = call.args.paymentInfo;
        result = this.api.getPaymentInfoAffinity(paymentInfo);
        break;
      default:
        throw new Error(`No such method '${call.method}'`);
    }

    return result;
  }

  render() {
    return null;
  }
}

export default RNMethodChannel;
