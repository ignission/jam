import React from 'react';
import Video from 'components/atoms/Video';
import { StreamManager } from 'openvidu-browser';
import './UserVideo.css';

interface Props {
  streamManager: StreamManager;
}

const UserVideo: React.FC<Props> = (props) => {
  const getNicknameTag = () => {
    // Gets the nickName of the user
    return JSON.parse(props.streamManager.stream.connection.data).clientData;
  };

  return (
    <div className="streamcomponent">
      <Video streamManager={props.streamManager} />
      <div>
        <p>{getNicknameTag()}</p>
      </div>
    </div>
  );
};

export default UserVideo;
