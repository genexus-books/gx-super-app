import React, { Component } from 'react';
import ExampleSuperApp from '../../example_superapp';
import MiniApp from '../../model/miniapp'
import MiniAppsSkeleton from "../miniapps_skeleton";

import { FlatList, Image, Text, TouchableOpacity, View } from 'react-native';
import { defaultPadding } from '../constants';

interface CardProps {
  children: React.ReactNode;
}

interface NetworkMiniAppsListProps {}

interface NetworkMiniAppsListState {
  miniApps: MiniApp[];
  isLoading: boolean;
}

class NetworkMiniAppsList extends Component<NetworkMiniAppsListProps, NetworkMiniAppsListState> {
  private _plugin: ExampleSuperApp;
  constructor(props: NetworkMiniAppsListProps) {
    super(props);
    this.state = {
      miniApps: [],
      isLoading: true,
    };

    this._plugin = new ExampleSuperApp();
  }
  
  static label = 'Network';
  static routeName = '/networkList';
  static icon = 'list';

  componentDidMount() {
    this.initMiniAppsList();
  }

  initMiniAppsList = async () => {
    let newMiniApps = null;
    try {
      newMiniApps = await this._plugin.getMiniApps('');
    } catch (error) {
      newMiniApps = null;
    }

    if (newMiniApps) {
      this.setState({ miniApps: newMiniApps, isLoading: false });
    }
  };

  renderMiniApp = ({ item: MiniApp }) => {
    return (
      <TouchableOpacity onPress={() => this._plugin.loadMiniApp(item)}>
        <View style={{ padding: 16 }}>
          <Image source={{ uri: item.iconUrl }} style={{ width: 40, height: 40, marginRight: 8 }} />
          <View style={{ flex: 1 }}>
            <Text>{item.name}</Text>
            <Text>{item.description}</Text>
          </View>
        </View>
      </TouchableOpacity>
    );
  };

  render() {
    const { miniApps, isLoading } = this.state;

    return (
      <View>
        {isLoading ? (
          <FlatList
            data={[...Array(5)]}
            keyExtractor={(_, index) => index.toString()}
            renderItem={() => <MiniAppsSkeleton />}
            ItemSeparatorComponent={() => <View style={{ height: defaultPadding }} />}
          />
        ) : (
          <FlatList
            data={miniApps}
            keyExtractor={(item) => item.name}
            renderItem={this.renderMiniApp}
            ItemSeparatorComponent={() => <View style={{ height: defaultPadding }} />}
          />
        )}
      </View>
    );
  }
}

export default NetworkMiniAppsList;
function item(item: any): void {
  throw new Error('Function not implemented.');
}

