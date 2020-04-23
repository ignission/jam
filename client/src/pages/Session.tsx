import React from 'react';
import { StreamManager } from 'openvidu-browser';
import UserVideo from 'components/molecules/UserVideo';

interface Props {
  sessionId: string;
  mainStreamManager: StreamManager | null;
  publisher: StreamManager | null;
  subscribers: StreamManager[];
  handleMainVideoStream: (streamManager: StreamManager) => void;
  leaveSession: () => void;
}

export const Session: React.FC<Props> = (props) => {
  const publisher = props.publisher;

  return (
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
        <div id="main-video" className="col-md-6">
          <UserVideo streamManager={props.mainStreamManager} />
        </div>
      ) : null}
      <div id="video-container" className="col-md-6">
        {publisher !== null ? (
          <div
            className="stream-container col-md-6 col-xs-6"
            onClick={() => props.handleMainVideoStream(publisher)}
          >
            <UserVideo streamManager={publisher} />
          </div>
        ) : null}
        {props.subscribers.map((sub, i) => (
          <div
            key={i}
            className="stream-container col-md-6 col-xs-6"
            onClick={() => props.handleMainVideoStream(sub)}
          >
            <UserVideo streamManager={sub} />
          </div>
        ))}
      </div>
    </div>
  );
};
