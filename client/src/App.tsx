import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import {
  StreamManager,
  Publisher,
  Session,
  OpenVidu,
  StreamEvent,
} from 'openvidu-browser';
import Container from 'components/atoms/Container';
import { Login } from 'pages/Login';
import { Session as SessionView } from 'pages/Session';
import { WindowServiceImpl } from 'services/WindowService';
import { OpenViduServiceImpl } from 'services/OpenViduService';

const OPENVIDU_SERVER_URL = 'https://localhost:4443';
const OPENVIDU_SERVER_SECRET = 'MY_SECRET';

const App: React.FC = () => {
  const [mySessionId, setSessionId] = useState<string>('SessionA');
  const [myUserName, setUserName] = useState<string>(
    'Participant' + Math.floor(Math.random() * 100)
  );
  const [session, setSession] = useState<Session | null>(null);
  const [
    mainStreamManager,
    setMainStreamManager,
  ] = useState<StreamManager | null>(null);
  const [publisher, setPublisher] = useState<Publisher | null>(null);
  const [subscribers, setSubscribers] = useState<StreamManager[]>([]);
  const ovRef = useRef<OpenVidu | null>(null);

  // services
  const windowService = WindowServiceImpl(window);
  const openViduService = OpenViduServiceImpl(
    OPENVIDU_SERVER_URL,
    OPENVIDU_SERVER_SECRET,
    windowService
  );

  const handleMainVideoStream = (streamManager: StreamManager) => {
    if (mainStreamManager === streamManager) return;
    setMainStreamManager(streamManager);
  };

  const onBeforeUnload = () => leaveSession();

  const deleteSubscriber = (streamManager: StreamManager) => {
    let index = subscribers.indexOf(streamManager, 0);
    if (index > -1) {
      subscribers.splice(index, 1);
      setSubscribers(subscribers);
    }
  };

  useEffect(() => {
    // --- 3) Specify the actions when events take place in the session ---
    // On every new Stream received...
    session?.on('streamCreated', (event) => {
      // Subscribe to the Stream to receive it. Second parameter is undefined
      // so OpenVidu doesn't create an HTML video by its own
      var subscriber = session.subscribe((event as StreamEvent).stream, '');
      //   var subscribers = subscribers;
      subscribers.push(subscriber);
      // Update the state with the new subscribers
      setSubscribers(subscribers);
    });
    // On every Stream destroyed...
    session?.on('streamDestroyed', (event) => {
      // Remove the str(event as StreamEvent)rom 'subscribers' array
      deleteSubscriber((event as StreamEvent).stream.streamManager);
    });
    // --- 4) Connect to the session with a valid user token ---
    // 'getToken' method is simulating what your server-side should do.
    // 'token' parameter should be retrieved and returned by your own backend
    openViduService.getToken(mySessionId).then((token) => {
      // First param is the token got from OpenVidu Server. Second param can be retrieved by every user on event
      // 'streamCreated' (property Stream.connection.data), and will be appended to DOM as the user's nickname
      if (session === null) {
        console.log('getToken failed. session is null');
        return;
      }
      session
        .connect(token as string, { clientData: myUserName })
        .then(() => {
          ovRef.current?.getDevices().then((devs) => {
            console.log(devs);
          });
          // --- 5) Get your own camera stream ---
          // Init a publisher passing undefined as targetElement (we don't want OpenVidu to insert a video
          // element: we will manage it on our own) and with the desired properties
          if (ovRef.current === null) {
            console.log('initPublisher failed. ovRef is null');
            return;
          }
          let publisher = ovRef.current.initPublisher('', {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
            publishVideo: true, // Whether you want to start publishing with your video enabled or not
            resolution: '640x480', // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
            mirror: false, // Whether to mirror your local video or not
          });
          // --- 6) Publish your stream ---
          session.publish(publisher);
          // Set the main video in the page to display our webcam and store our Publisher
          setMainStreamManager(publisher);
          setPublisher(publisher);
        })
        .catch((error) => {
          console.log(
            'There was an error connecting to the session:',
            error.code,
            error.message
          );
        });
    });
  }, [session]);

  const joinSession = () => {
    // --- 1) Get an OpenVidu object ---
    ovRef.current = new OpenVidu();

    // --- 2) Init a session ---
    setSession(ovRef.current.initSession());
  };

  const leaveSession = () => {
    // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
    if (session) {
      session.disconnect();
    }
    // Empty all properties...
    ovRef.current = null;
    setSession(null);
    setSubscribers([]);
    setSessionId('SessionA');
    setUserName('Participant' + Math.floor(Math.random() * 100));
    setMainStreamManager(null);
    setPublisher(null);
  };

  /**
   * --------------------------
   * SERVER-SIDE RESPONSIBILITY
   * --------------------------
   * These methods retrieve the mandatory user token from OpenVidu Server.
   * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
   * the API REST, openvidu-java-client or openvidu-node-client):
   *   1) Initialize a session in OpenVidu Server	(POST /api/sessions)
   *   2) Generate a token in OpenVidu Server		(POST /api/tokens)
   *   3) The token must be consumed in Session.connect() method
   */

  useEffect(() => {
    window.addEventListener('beforeunload', onBeforeUnload);

    return () => window.removeEventListener('beforeunload', onBeforeUnload);
  }, []);

  if (session === null) {
    return (
      <Container>
        <Login
          userName={myUserName}
          sessionId={mySessionId}
          joinSession={joinSession}
          setUserName={(e) => setUserName(e.target.value)}
          setSessionId={(e) => setSessionId(e.target.value)}
        />
      </Container>
    );
  }

  return (
    <Container>
      <SessionView
        sessionId={mySessionId}
        mainStreamManager={mainStreamManager}
        publisher={publisher}
        subscribers={subscribers}
        handleMainVideoStream={handleMainVideoStream}
        leaveSession={leaveSession}
      />
    </Container>
  );
};

export default App;
