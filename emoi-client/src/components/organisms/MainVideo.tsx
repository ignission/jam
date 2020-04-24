import React from 'react';
import UserVideo from 'components/molecules/UserVideo';
import { StreamManager } from 'openvidu-browser';

interface Props {
  streamManager: StreamManager;
}

export const MainVideo: React.FC<Props> = ({ streamManager }) => (
  <div id="main-video" className="col-md-6">
    <UserVideo streamManager={streamManager} />
  </div>
);
