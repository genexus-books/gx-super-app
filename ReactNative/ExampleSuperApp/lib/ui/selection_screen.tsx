import React from 'react';
import { Text, View, Button } from 'react-native';

interface SelectionScreenProps {
  navigation: any;
  totalAmount: number;
}

class SelectionScreen extends React.Component<SelectionScreenProps> {
  constructor(props: SelectionScreenProps) {
    super(props);
  }

  render() {
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <Text style={{ fontSize: 20, marginBottom: 20 }}>Confirm payment?</Text>
        <Button
          title="Confirm"
          onPress={() => {
            this.props.navigation.goBack('Confirm');
          }}
        />
        <Button
          title="Cancel"
          onPress={() => {
            this.props.navigation.goBack('Cancel');
          }}
        />
      </View>
    );
  }
}

export default SelectionScreen;