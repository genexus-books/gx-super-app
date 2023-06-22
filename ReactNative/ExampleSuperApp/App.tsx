import React, { useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

import { TabItem, tabItems } from './lib/routes'
import { defaultPadding } from './lib/ui/constants';

const App = () => {

  const [selectedTab, setSelectedTab] = useState(0);
  
  const renderTabItems = () => {
    return tabItems.map((item: TabItem, index: number) => (
      <TouchableOpacity
        key={item.label}
        style={{ flex: 1, alignItems: 'center', justifyContent: 'center', padding: defaultPadding }}
        onPress={() => setSelectedTab(index)}
      >
        <Icon
            style={{ padding: 12 }}
            name={item.icon}
            color={(selectedTab === index) ? '#006CFF' : '#7D7D7D'}
            size={20}
          />
        <Text style={{ color: (selectedTab === index) ? '#006CFF' : '#7D7D7D', fontWeight: selectedTab === index ? 'bold' : 'normal' }}>{item.label}</Text>
      </TouchableOpacity>
    ));
  };

  return (
    <View style={{ flex: 1,  paddingTop: 50}}>
      <View style={{ backgroundColor: 'White'}}>
        <Text style={{ color: 'black', fontSize: 24, fontWeight: 'bold', textAlign:'center' }}>{(selectedTab ) ? 'Cache' : 'Mini Apps'}</Text>
      </View>
      <View style={{ flex: 1,  }}>
        {tabItems[selectedTab].widget}
      </View>
      <View style={{ flexDirection: 'row', borderTopWidth: 0 }}>
        {renderTabItems()}
      </View>
    </View>
  );
};

export default App;
