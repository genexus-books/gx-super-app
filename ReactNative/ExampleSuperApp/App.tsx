import React from "react";
import MainScreen, { MainScreenState } from "./lib/ui/main_screen";

export interface MainScreenStateContext {
  state: MainScreenState;
  setState: React.Dispatch<React.SetStateAction<MainScreenState>>;
}

export const UserContext = React.createContext<MainScreenStateContext | null>(null);

const App = () => {
  const [state, setState] = React.useState<MainScreenState>({
    selectedIndex: 0,
  });

  const contextValue: MainScreenStateContext = {
    state,
    setState
  };

  return (
    <UserContext.Provider value={contextValue}>
      <MainScreen />
    </UserContext.Provider>
  );
};

export default App;
