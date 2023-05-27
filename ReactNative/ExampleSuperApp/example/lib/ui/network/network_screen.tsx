import React, { Component } from 'react';
import ExampleSuperApp from "../../../../lib/example_superapp";
import MiniApp from "../../../../lib/model/miniapp";
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
  constructor(props: NetworkMiniAppsListProps) {
    super(props);
    this.state = {
      miniApps: [],
      isLoading: true,
    };
  }
  
  static label = 'Network';
  static routeName = '/networkList';
  static icon = 'list';

  componentDidMount() {
    this.fetchMiniApps();
  }

  fetchMiniApps = async () => {
    try {
      const newMiniApps = await ExampleSuperApp.getMiniApps('');
      if (newMiniApps != null) {
        this.setState({ miniApps: newMiniApps || [] });
      }
    } catch (error) {
      console.log(error);
    } finally {
      this.setState({ isLoading: false });
    }
  };

  Card: React.FC<CardProps> = ({ children }) => {
    return (
      <View
        style={{
          borderWidth: 1,
          borderColor: '#ccc',
          borderRadius: 8,
          padding: 16,
          marginBottom: 16,
        }}
      >
        {children}
      </View>
    );
  };

  renderMiniAppItem = ({ item }: { item: MiniApp }) => {
    return (
      <this.Card>
        <TouchableOpacity onPress={() => ExampleSuperApp.loadMiniApp(item)}>
          <Text style={{ fontSize: 16, fontWeight: 'bold', marginBottom: 8 }}>
            {item.name}
          </Text>
          <Text>{item.description}</Text>
          <Image source={{ uri: item.iconUrl }} style={{ marginTop: 8 }} />
        </TouchableOpacity>
      </this.Card>
    );
  };

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
            keyExtractor={(item) => item.id}
            renderItem={this.renderMiniAppItem}
            ItemSeparatorComponent={() => <View style={{ height: defaultPadding }} />}
          />
        )}
      </View>
    );
  }
}

export default NetworkMiniAppsList;
