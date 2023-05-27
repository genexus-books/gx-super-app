import React from 'react';
import { View, StyleSheet } from 'react-native';
import { Colors } from './constants';

interface SkeletonProps {
  height?: number;
  width?: number;
}

const Skeleton: React.FC<SkeletonProps> = ({ height, width }) => {
  return (
    <View
      style={[
        styles.container,
        { height: height, width: width },
      ]}
    ></View>
  );
};

interface CircleSkeletonProps {
  size?: number;
}

const CircleSkeleton: React.FC<CircleSkeletonProps> = ({ size }) => {
  return <View style={[styles.circle, { width: size, height: size }]}></View>;
};

const styles = StyleSheet.create({
  container: {
    padding: 8,
    backgroundColor: Colors.black + "26",
    borderRadius: 8,
  },
  circle: {
    backgroundColor: Colors.primaryColor + "26",
    borderRadius: 50,
  },
});

export { Skeleton, CircleSkeleton };
