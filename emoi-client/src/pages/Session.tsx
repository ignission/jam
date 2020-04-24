import React from 'react';
import { StreamManager } from 'openvidu-browser';
import { MainVideo } from 'components/organisms/MainVideo';
import { ParticipantVideo } from 'components/organisms/ParticipantVideo';

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
      </div>
    </div>
  );
};
