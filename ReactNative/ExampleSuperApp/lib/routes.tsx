import React from 'react';
import CachedMiniAppsList from './ui/cache/cached_screen';
import NetworkMiniAppsList from './ui/network/network_screen';

class TabItem {
  label: string;
  route: string;
  icon: string;   
  widget: JSX.Element;

  constructor(label: string, route: string, icon: string, widget: JSX.Element) {
    this.label = label;
    this.route = route;
    this.icon = icon;
    this.widget = widget;
  }
}

const tabItems: TabItem[] = [
  new TabItem(NetworkMiniAppsList.label, 
              NetworkMiniAppsList.routeName, 
              NetworkMiniAppsList.icon, 
              <NetworkMiniAppsList />),
  new TabItem(CachedMiniAppsList.label, 
              CachedMiniAppsList.routeName, 
              CachedMiniAppsList.icon, 
              <CachedMiniAppsList />),
];

export { TabItem, tabItems };
