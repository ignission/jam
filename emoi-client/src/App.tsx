import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import loadable from '@loadable/component';
import { WindowServiceImpl } from 'WindowService';
import { OpenViduClientImpl } from 'OpenViduClient';
import { APIClientOnAxios } from 'network/APIClient';

const Home = loadable(() => import('components/pages/Home'));
const Room = loadable(() =>
  import('components/pages/Room').then(({ Room }) => Room)
);

const App: React.FC = () => {
  // services
  const client = APIClientOnAxios('');

  // client.listSessions().then((data) => console.dir(data));
  // client.generateToken('test-session').then((data) => console.dir(data));

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
