import React, { Component } from 'react';
import ExampleSuperApp from '../../example_superapp';
import MiniApp from '../../model/miniapp'
import MiniAppsSkeleton from "../miniapps_skeleton";

import { FlatList, Image, Text, TouchableOpacity, View } from 'react-native';
import { defaultPadding } from '../constants';

interface NetworkMiniAppsListState {
  miniApps: MiniApp[];
  isLoading: boolean;
}

class NetworkMiniAppsList extends React.Component<{}, NetworkMiniAppsListState> {
  plugin: ExampleSuperApp;

  static label = 'Network';
  static routeName = '/networkList';
  static icon = 'list';

  constructor(props: {}) {
    super(props);
    this.state = {
      miniApps: [],
      isLoading: true,
    };
    this.plugin = new ExampleSuperApp(); 
  }

  componentDidMount() {
    this.initMiniAppsList();
  }

  initMiniAppsList = async () => {
    try {
      const newMiniApps = await this.plugin.getMiniApps('');
      this.setState({
        miniApps: newMiniApps || [],
        isLoading: false,
      });
    } catch (error) {
      console.error('Error retrieving mini-apps:', error);
      this.setState({
        miniApps: [],
        isLoading: false,
      });
    }
  };

  renderItem = ({ item }: { item: MiniApp }) => (
    <TouchableOpacity onPress={() => this.plugin.loadMiniApp(item)}>
      <View style={{ padding: 10 }}>
        <Image style={{ width: 50, height: 50 }} source={{ uri: item.iconUrl }} />
        <Text>{item.name}</Text>
        <Text>{item.description}</Text>
      </View>
    </TouchableOpacity>
  );

  render() {
    const { miniApps, isLoading } = this.state;
    return (
      <View>
        {isLoading ? (
          <FlatList
            data={[1, 2, 3, 4, 5]}
            keyExtractor={(item) => item.toString()}
            renderItem={() => <MiniAppsSkeleton />}
            ItemSeparatorComponent={() => <View style={{ height: defaultPadding }} />}
          />
        ) : (
          <FlatList
            data={miniApps}
            keyExtractor={(item) => item.id.toString()}
            renderItem={this.renderItem}
            ItemSeparatorComponent={() => <View style={{ height: defaultPadding }} />}
          />
        )}
      </View>
    );
  }
}

export default NetworkMiniAppsList;