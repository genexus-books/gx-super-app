import React from 'react';
import ExampleSuperApp from '../../example_superapp';
import MiniApp from '../../model/miniapp'

import { FlatList, Image, RefreshControl, Text, TouchableOpacity, View } from 'react-native';
import { defaultPadding } from '../constants';

interface NetworkMiniAppsListProps {}

interface NetworkMiniAppsListState {
  miniApps: MiniApp[];
  isLoading: boolean;
  refreshing: boolean;
}

class NetworkMiniAppsList extends React.Component<NetworkMiniAppsListProps, NetworkMiniAppsListState> {
  plugin: ExampleSuperApp;

  static label = 'Network';
  static routeName = '/networkList';
  static icon = 'list';

  constructor(props: NetworkMiniAppsListProps) {
    super(props);
    this.state = {
      miniApps: [],
      isLoading: true,
      refreshing: false,
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

  onRefresh = () => {
    this.setState({ refreshing: true });
    setTimeout(() => {
      this.setState({ refreshing: false });
    }, 500);
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
    const { miniApps, isLoading, refreshing } = this.state;

    return (
      <View>
        {isLoading ? (
          <FlatList
          data={miniApps}
          keyExtractor={(item) => item.toString()}
          renderItem={this.renderItem}
          refreshControl={
            <RefreshControl refreshing={isLoading} onRefresh={this.onRefresh} />
          }
        />
          
        ) : (
          <FlatList
            data={miniApps}
            keyExtractor={(item) => item.id.toString()}
            renderItem={this.renderItem}
            ItemSeparatorComponent={() => <View style={{ height: 20 }} />}
            refreshControl={<RefreshControl refreshing={refreshing} onRefresh={this.onRefresh} />}
          />
        )}
      </View>
    );
  }
}

export default NetworkMiniAppsList;
