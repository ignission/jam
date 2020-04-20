import React from 'react';
import Video from 'Video';
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

/**
export default class UserVideoComponent extends Component {

    getNicknameTag() {
        // Gets the nickName of the user
        return JSON.parse(this.props.streamManager.stream.connection.data).clientData;
    }

    render() {
        return (
            <div>
                {this.props.streamManager !== undefined ? (
                    <div className="streamcomponent">
                        <OpenViduVideoComponent streamManager={this.props.streamManager} />
                        <div><p>{this.getNicknameTag()}</p></div>
                    </div>
                ) : null}
            </div>
        );
    }
}
 */
