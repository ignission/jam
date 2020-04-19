import React, { useEffect, useRef } from 'react';
import { StreamManager } from 'openvidu-browser';

interface Props {
  streamManager: StreamManager;
}

const OpenViduVideoComponent: React.FC<Props> = (props) => {
  let ref = useRef(null);

  useEffect(() => {
    if (ref.current === null) {
      ref = React.createRef<HTMLVideoElement>();
      props.streamManager.addVideoElement(ref.current);
    }
  }, []);

  return <video autoPlay={true} ref={ref} />;
};

export default OpenViduVideoComponent;

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
