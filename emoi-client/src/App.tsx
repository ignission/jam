import React, { useState, useEffect, useRef } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import {
  StreamManager,
  Publisher,
  Session,
  OpenVidu,
  StreamEvent,
} from 'openvidu-browser';
import Home from 'components/pages/Home';
import { WindowServiceImpl } from 'WindowService';
import { OpenViduClientImpl } from 'OpenViduClient';
import { Room } from 'components/pages/Room';

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
