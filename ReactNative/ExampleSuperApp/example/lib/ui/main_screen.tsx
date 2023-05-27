import React, { useContext } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

import { MainScreenStateContext, UserContext } from '../../../App';
import RNMethodChannel from '../../../lib/plugin/methods/rn_method_channel';
import { TabItem, tabItems } from '../routes';

export interface MainScreenState {
  selectedIndex: number;
}

interface MainScreenProps {
  context: MainScreenStateContext;
}

class MainScreen extends React.Component<MainScreenProps> {
  componentDidMount() {
    new RNMethodChannel({ buildContext: UserContext });
  }

  render() {
    const { selectedIndex } = this.props.context.state;
    const pages = tabItems.map((v) => v.widget);

    return (
      <View style={styles.container}>
        <Text style={styles.title}>Example SuperApp</Text>
        <View style={styles.content}>{pages[selectedIndex]}</View>
        <View style={styles.bottomNavigation}>{this.getBottomTabs(tabItems, selectedIndex)}</View>
      </View>
    );
  }

  onItemTapped(index: number) {
    this.props.context.setState({ selectedIndex: index });
  }

  getBottomTabs(tabs: TabItem[], selectedIndex: number) {
    return tabs.map((item, index) => (
      <TouchableOpacity
        key={index}
        style={styles.tabItem}
        onPress={() => this.onItemTapped(index)}
      >
        <Icon name={item.icon} size={30} color={selectedIndex === index ? 'blue' : 'gray'} />
        <Text style={[styles.tabLabel, selectedIndex === index && styles.selectedLabel]}>
          {item.label}
        </Text>
      </TouchableOpacity>
    ));
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginVertical: 16,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  bottomNavigation: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: 'gray',
    paddingVertical: 8,
  },
  tabItem: {
    alignItems: 'center',
  },
  tabLabel: {
    fontSize: 15,
    fontWeight: 'bold',
    marginTop: 4,
  },
  selectedLabel: {
    color: 'blue',
  },
});

export default MainScreen;
