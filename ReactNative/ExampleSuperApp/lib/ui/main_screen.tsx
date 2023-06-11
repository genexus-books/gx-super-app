import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, Button } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';


import { MainScreenStateContext, UserContext } from '../../App';
import SuperAppAPI from '../APIs/SupperAppAPI';
import ExampleSuperapp from '../example_superapp'
import { TabItem, tabItems } from '../routes';//example_superapp

export interface MainScreenState {
  selectedIndex: number;
}

interface MainScreenProps {
  context: MainScreenStateContext;
}

const MainScreen = () => {

  const [selectedTab, setSelectedTab] = useState(0);
  const [currentPage, setCurrentPage] = useState<JSX.Element>(tabItems[0].widget);

  const handleTabPress = (index: number) => {
    setSelectedTab(index);
    setCurrentPage(tabItems[index].widget);

  };
  useEffect(() => {
    // Example usage

  }, []);
  
  const renderTabItems = () => {
    return tabItems.map((item: TabItem, index: number) => (
      <TouchableOpacity
        key={item.label}
        style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}
        onPress={() => setSelectedTab(index)}
      >
        <Text style={{ fontWeight: selectedTab === index ? 'bold' : 'normal' }}>{item.label}</Text>
      </TouchableOpacity>
    ));
  };

  return (
    <View style={{ flex: 1 }}>
      <View style={{ backgroundColor: 'blue', padding: 16 }}>
        <Text style={{ color: 'white', fontSize: 18, fontWeight: 'bold' }}>Example SuperApp</Text>
      </View>
      <View style={{ flex: 1 }}>
        {tabItems[selectedTab].widget}
      </View>
      <View style={{ flexDirection: 'row', borderTopWidth: 1 }}>
        {renderTabItems()}
      </View>
    </View>
  );
};

export default MainScreen;


