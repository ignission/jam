import React from 'react';
import { StreamManager } from 'openvidu-browser';
import Video from 'components/atoms/Video';
import styled from '@emotion/styled';

interface Props {
  readonly streamManager: StreamManager;
}

const Stream = styled.div({
  '& > video': {
    width: '100%',
    height: 'auto',
    float: 'left',
    cursor: 'pointer'
  }
})

const Nickname = styled.div({
  position: 'absolute',
  background: '#f8f8f8',
  paddingLeft: '5px',
  paddingRight: '5px',
  color: '#777777',
  fontWeight: 'bold',
  borderBottomRightRadius: '4px',
  '& > p': {
    margin: 0
  }
})

export const UserVideo: React.FC<Props> = ({ streamManager }) => {
  const nickname = JSON.parse(streamManager.stream.connection.data).clientData;
  return (
    <Stream>
      <Video streamManager={streamManager} />
      <Nickname><p>{nickname}</p></Nickname>
    </Stream>
  )
}
