import React from 'react';
import { View, Text, StyleSheet, Image, TouchableHighlight, FlatList } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import ExampleSuperApp from '../../example_superapp';
import MiniApp from '../../model/miniapp'

interface CachedMiniAppsListProps {
}

interface CachedMiniAppsListState {
  miniApps: MiniApp[];
}

class CachedMiniAppsList extends React.Component<
  CachedMiniAppsListProps,
  CachedMiniAppsListState
> {
  static label = 'History';
  static routeName = '/cachedList';
  static icon = 'time'
  static _plugin = new ExampleSuperApp()

  constructor(props: CachedMiniAppsListProps) {
    super(props);
    this.state = {
      miniApps: [],
    };
  }

  componentDidMount() {
    this.initMiniAppsList();
  }

  async initMiniAppsList() {
    try {
      const newMiniApps = await CachedMiniAppsList._plugin.getCachedMiniApps();
      if (newMiniApps != null) {
        this.setState({ miniApps: newMiniApps });
      }
    } catch (error) {
      console.error(error);
    }
  }

  hasValue(value: string | undefined | null): boolean {
    return value != null && value !== '';
  }

  renderEmptyList() {
    return (
      <View style={styles.emptyContainer}>
        <Text>You haven't downloaded any MiniApps yet!</Text>
      </View>
    );
  }

  renderMiniAppItem = ({ item }: { item: MiniApp }) => {
    const timestamp = 709085944.24299502; // Unix timestamp

  const date = new Date(timestamp * 1000); // Convert to milliseconds

  const formattedDate = date.toLocaleString(); // Format the date string
    return (
      <TouchableHighlight
        style={styles.miniAppItem}
        onPress={() => this.handleMiniAppPress(item)}
      >
        <View style={styles.miniAppItemContent} >
          {this.hasValue(item.iconUrl) ? (
            <Image source={{ uri: item.iconUrl }} style={styles.miniAppIcon} />
          ) : (
            <Icon name="information-circle" size={30} color="gray" />
          )}
          <View style={styles.miniAppTextContainer}>
            <Text style={styles.miniAppName}>{`${item.id}`}</Text>

            <Text style={styles.miniAppInfo}>{`Creation date: ${formattedDate}`}</Text>
            <Text style={styles.miniAppInfo}>{`Last used date: ${item.lastUsedDate}`}</Text>
          </View>
        </View>
      </TouchableHighlight>
    );
  };

  handleMiniAppPress(miniApp: MiniApp) {
    CachedMiniAppsList._plugin.loadMiniApp(miniApp);
  }

  render() {
    const { miniApps } = this.state;

    return (
      <View style={styles.container}>
        {miniApps.length === 0 ? (
          this.renderEmptyList()
        ) : (
          <FlatList
            data={miniApps}
            keyExtractor={(miniApp) => miniApp.id}
            renderItem={this.renderMiniAppItem}
          />
        )}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  miniAppItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: 'gray',
  },
  miniAppItemContent: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  miniAppIcon: {
    width: 40,
    height: 40,
    marginRight: 16,
  },
  miniAppTextContainer: {
    flex: 1,
  },
  miniAppName: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  miniAppInfo: {
    fontSize: 12,
    color: 'gray',
  },
});

export default CachedMiniAppsList;