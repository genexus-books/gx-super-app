import React from 'react';
import { View, Text } from 'react-native';
import { Skeleton } from './skeleton';
import { defaultPadding } from './constants';

const MiniAppsSkeleton = () => {
return (
<View style={{ flexDirection: 'row' }}>
<Skeleton height={120} width={120} />
<View style={{ marginLeft: defaultPadding }}>
<View style={{ width: 80 }}>
<Skeleton />
</View>
<View style={{ marginTop: defaultPadding / 2 }}>
<Skeleton />
</View>
<View style={{ marginTop: defaultPadding / 2 }}>
<Skeleton />
</View>
<View style={{ marginTop: defaultPadding / 2 }}>
<View style={{ flexDirection: 'row' }}>
<View style={{ flex: 1 }}>
<Skeleton />
</View>
<View style={{ marginLeft: defaultPadding, flex: 1 }}>
<Skeleton />
</View>
</View>
</View>
</View>
</View>
);
};

export default MiniAppsSkeleton;