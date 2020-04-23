import React from 'react';
import UserVideo from 'components/molecules/UserVideo';
import { StreamManager } from 'openvidu-browser';

interface Props {
  streamManager: StreamManager;
  handleMainVideoStream: (streamManager: StreamManager) => void;
}

export const ParticipantVideo: React.FC<Props> = (props) => (
  <div
    className="stream-container col-md-6 col-xs-6"
    onClick={() => props.handleMainVideoStream(props.streamManager)}
  >
    <UserVideo streamManager={props.streamManager} />
  </div>
);
