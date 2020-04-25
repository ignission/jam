import React, { useState } from 'react';
import { StreamManager, Session, Publisher } from 'openvidu-browser';
import { Option, map } from 'fp-ts/es6/Option';
import { MainVideo } from 'components/organisms/MainVideo';
import { ParticipantVideo } from 'components/organisms/ParticipantVideo';
import { Chat } from 'components/organisms/chat';
import Container from 'components/atoms/Container';
import { User } from 'models/User';
import { Toolbar } from 'components/organisms/toolbar';

interface Props {
  sessionId: string;
  session: Option<Session>;
  mainStreamManager: Option<StreamManager>;
  publisher: Option<Publisher>;
  subscribers: StreamManager[];
  handleMainVideoStream: (streamManager: StreamManager) => void;
  leaveSession: () => void;
}

export const SessionView: React.FC<Props> = (props) => {
  const publisher = props.publisher;
  const [localUser, setLocalUser] = useState<User>(User());
  const [chatDisplay, setChatDisplay] = useState<string>('none');
  const [messageReceived, setMessageReceived] = useState<boolean>(false);

  const camStatusChanged = () => {
    const updated = localUser.setVideoActive(!localUser.isVideoActive());
    const f = map((sm: StreamManager) =>
      sm.publishVideo(updated.isVideoActive())
    );

    f(updated.getStreamManager());
    sendSignalUserChanged({ isVideoActive: updated.isVideoActive() });
    setLocalUser(updated);
  };

  const micStatusChanged = () => {
    const updated = localUser.setAudioActive(!localUser.isAudioActive());
    const f = map((sm: StreamManager) =>
      sm.publishAudio(updated.isAudioActive())
    );

    f(updated.getStreamManager());
    sendSignalUserChanged({ isAudioActive: updated.isAudioActive() });
    setLocalUser(updated);
  };

  const sendSignalUserChanged = (data: any) => {
    const signalOptions = {
      data: JSON.stringify(data),
      type: 'userChanged',
    };
    const f = map((s: Session) => s.signal(signalOptions));

    f(props.session);
  };

  const toggleChat = () => {
    let display = property;

    if (display === undefined) {
      display = chatDisplay === 'none' ? 'block' : 'none';
    }
    if (display === 'block') {
      setMessageReceived(false);
      setChatDisplay(display);
    } else {
      console.log('chat', display);
      setChatDisplay(display);
    }
    this.updateLayout();
  };

  return (
    <Container>
      <Toolbar
        sessionId={props.sessionId}
        user={localUser}
        showNotification={messageReceived}
        camStatusChanged={camStatusChanged}
        micStatusChanged={micStatusChanged}
        screenShare={screenShare}
        stopScreenShare={stopScreenShare}
        toggleFullscreen={toggleFullscreen}
        leaveSession={props.leaveSession}
        toggleChat={toggleChat}
      />
      <div id="session">
        <div id="session-header">
          <h1 id="session-title">{props.sessionId}</h1>
          <input
            className="btn btn-large btn-danger"
            type="button"
            id="buttonLeaveSession"
            onClick={props.leaveSession}
            value="Leave session"
          />
        </div>

        {props.mainStreamManager !== null ? (
          <MainVideo streamManager={props.mainStreamManager} />
        ) : null}
        <div id="video-container" className="col-md-6">
          {publisher !== null ? (
            <ParticipantVideo
              streamManager={publisher}
              handleMainVideoStream={props.handleMainVideoStream}
            />
          ) : null}
          {props.subscribers.map((sub, i) => (
            <ParticipantVideo
              key={i}
              streamManager={sub}
              handleMainVideoStream={props.handleMainVideoStream}
            />
          ))}
          <Chat
            user={localUser}
            chatDisplay={chatDisplay}
            close={toggleChat}
            messageReceived={checkNotification}
          />
        </div>
      </div>
    </Container>
  );
};
