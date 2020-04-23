import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import {
  StreamManager,
  Publisher,
  Session,
  OpenVidu,
  StreamEvent,
} from 'openvidu-browser';
import UserVideo from 'components/organisms/UserVideo';

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
    getToken().then((token) => {
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

  const getToken = () => {
    return createSession(mySessionId).then((sessionId) =>
      createToken(sessionId)
    );
  };

  const createSession = (sessionId: any) => {
    return new Promise((resolve, reject) => {
      var data = JSON.stringify({ customSessionId: sessionId });
      axios
        .post(OPENVIDU_SERVER_URL + '/api/sessions', data, {
          headers: {
            Authorization:
              'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          console.log('CREATE SESION', response);
          resolve(response.data.id);
        })
        .catch((response) => {
          var error = Object.assign({}, response);
          if (error.response.status === 409) {
            resolve(sessionId);
          } else {
            console.log(error);
            console.warn(
              'No connection to OpenVidu Server. This may be a certificate error at ' +
                OPENVIDU_SERVER_URL
            );
            if (
              window.confirm(
                'No connection to OpenVidu Server. This may be a certificate error at "' +
                  OPENVIDU_SERVER_URL +
                  '"\n\nClick OK to navigate and accept it. ' +
                  'If no certificate warning is shown, then check that your OpenVidu Server is up and running at "' +
                  OPENVIDU_SERVER_URL +
                  '"'
              )
            ) {
              window.location.assign(
                OPENVIDU_SERVER_URL + '/accept-certificate'
              );
            }
          }
        });
    });
  };

  const createToken = (sessionId: any) => {
    return new Promise((resolve, reject) => {
      var data = JSON.stringify({ session: sessionId });
      axios
        .post(OPENVIDU_SERVER_URL + '/api/tokens', data, {
          headers: {
            Authorization:
              'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SERVER_SECRET),
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          console.log('TOKEN', response);
          resolve(response.data.token);
        })
        .catch((error) => reject(error));
    });
  };

  useEffect(() => {
    window.addEventListener('beforeunload', onBeforeUnload);

    return () => window.removeEventListener('beforeunload', onBeforeUnload);
  }, []);

  if (session === null) {
    return (
      <div className="container">
        <div id="join">
          <div id="img-div">
            <img
              src="images/openvidu_grey_bg_transp_cropped.png"
              alt="OpenVidu logo"
            />
          </div>
          <div id="join-dialog" className="jumbotron vertical-center">
            <h1> Join a video session </h1>
            <form className="form-group" onSubmit={joinSession}>
              <p>
                <label>Participant: </label>
                <input
                  className="form-control"
                  type="text"
                  id="userName"
                  value={myUserName}
                  onChange={(e) => setUserName(e.target.value)}
                  required
                />
              </p>
              <p>
                <label> Session: </label>
                <input
                  className="form-control"
                  type="text"
                  id="sessionId"
                  value={mySessionId}
                  onChange={(e) => setSessionId(e.target.value)}
                  required
                />
              </p>
              <p className="text-center">
                <input
                  className="btn btn-lg btn-success"
                  name="commit"
                  type="submit"
                  value="JOIN"
                />
              </p>
            </form>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div id="session">
        <div id="session-header">
          <h1 id="session-title">{mySessionId}</h1>
          <input
            className="btn btn-large btn-danger"
            type="button"
            id="buttonLeaveSession"
            onClick={leaveSession}
            value="Leave session"
          />
        </div>

        {mainStreamManager !== null ? (
          <div id="main-video" className="col-md-6">
            <UserVideo streamManager={mainStreamManager} />
          </div>
        ) : null}
        <div id="video-container" className="col-md-6">
          {publisher !== null ? (
            <div
              className="stream-container col-md-6 col-xs-6"
              onClick={() => handleMainVideoStream(publisher)}
            >
              <UserVideo streamManager={publisher} />
            </div>
          ) : null}
          {subscribers.map((sub, i) => (
            <div
              key={i}
              className="stream-container col-md-6 col-xs-6"
              onClick={() => handleMainVideoStream(sub)}
            >
              <UserVideo streamManager={sub} />
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default App;
