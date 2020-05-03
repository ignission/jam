import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import loadable from "@loadable/component";
import { WindowServiceImpl } from 'WindowService';
import { OpenViduClientImpl } from 'OpenViduClient';

const Home = loadable(() => import('components/pages/Home'))
const Room = loadable(() => import('components/pages/Room').then(({ Room }) => Room))

const OPENVIDU_SERVER_URL = 'https://localhost:4443';
const OPENVIDU_SERVER_SECRET = 'MY_SECRET';

const App: React.FC = () => {
  // services
  const windowService = WindowServiceImpl(window);
  const openViduClient = OpenViduClientImpl(
    OPENVIDU_SERVER_URL,
    OPENVIDU_SERVER_SECRET,
    windowService
  );
  return (
    <BrowserRouter>
      <Route
        exact
        path="/"
        render={(props) => (
          <Home onSubmit={(name) => props.history.push(name)} />
        )}
      />
      <Route path="/:id" component={Room} />
    </BrowserRouter>
  );
};

export default App;
