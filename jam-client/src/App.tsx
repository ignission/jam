import React, { useState } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import loadable from '@loadable/component';
// import { WindowServiceImpl } from 'WindowService';
// import { OpenViduClientImpl } from 'OpenViduClient';
import { APIClientOnAxios } from 'network/APIClient';

const Home = loadable(() => import('components/pages/Home'));
const Room = loadable(() =>
  import('components/pages/Room').then(({ Room }) => Room)
);
const Signin = loadable(() => import('components/pages/Signin'));
const Lobby = loadable(() =>
  import('components/pages/Lobby').then(({ Lobby }) => Lobby)
);

const App: React.FC = () => {
  const [initialName, setInitialName] = useState(
    'User_' + Math.floor(Math.random() * Math.floor(9999))
  );

  // services
  const client = APIClientOnAxios('');

  //client.createSession('test1').then((data) => console.dir(data));
  // client.listSessions().then((data) => console.dir(data));
  // client.generateToken('test-session').then((data) => console.dir(data));

  return (
    <BrowserRouter>
      <Route
        exact
        path="/"
        render={(props) => (
          <Home
            initialName={initialName}
            onSubmit={(name: string) => {
              setInitialName(name);
              props.history.push('/lobby');
            }}
          />
        )}
      />
      <Route path="/signin" component={Signin} />
      <Route
        path="/lobby"
        render={(props) => <Lobby userName={initialName} />}
      />
      <Route path="/rooms/:id" component={Room} />
    </BrowserRouter>
  );
};

export default App;
