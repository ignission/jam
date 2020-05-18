import React, { Component } from 'react';
import { StreamManager } from 'openvidu-browser';

interface Props {
  readonly streamManager: StreamManager;
}

export default class Video extends Component<Props> {
  private videoRef: React.RefObject<HTMLVideoElement>;

  constructor(props: Props) {
    super(props);
    this.videoRef = React.createRef();
  }

  componentDidUpdate() {
    if (!!this.videoRef.current) {
      this.props.streamManager.addVideoElement(this.videoRef.current);
    }
  }

  componentDidMount() {
    if (!!this.videoRef.current) {
      this.props.streamManager.addVideoElement(this.videoRef.current);
    }
  }

  render() {
    return <video autoPlay={true} ref={this.videoRef} />;
  }
}
