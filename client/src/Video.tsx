import React, { useEffect, useRef } from 'react';
import { StreamManager } from 'openvidu-browser';

interface Props {
  streamManager: StreamManager;
}

const VideoComponent: React.FC<Props> = (props) => {
  const ref = useRef<HTMLVideoElement>(null);
  const isInitialized = useRef<boolean>(false);

  useEffect(() => {
    if (isInitialized.current || ref.current === null) return;
    props.streamManager.addVideoElement(ref.current);
    isInitialized.current = true;
  }, []);

  return <video autoPlay={true} ref={ref} />;
};

export default VideoComponent;

/**
export default class OpenViduVideoComponent extends Component {

    constructor(props) {
        super(props);
        this.videoRef = React.createRef();
    }

    componentDidUpdate(props) {
        if (props && !!this.videoRef) {
            this.props.streamManager.addVideoElement(this.videoRef.current);
        }
    }

    componentDidMount() {
        if (this.props && !!this.videoRef) {
            this.props.streamManager.addVideoElement(this.videoRef.current);
        }
    }

    render() {
        return <video autoPlay={true} ref={this.videoRef} />;
    }

}
 */
