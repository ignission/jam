import React from 'react';
import styled from '@emotion/styled';
import * as Button from '../atoms/Button';
import Logo from '../atoms/Logo';

const Toolbar = styled.div({
  gridArea: 'Toolbar',
  display: 'grid',
  alignItems: 'center',
  gridTemplateColumns: '1fr 1fr 1fr',
  color: '#fff',
  backgroundColor: '#333',
  justifyContent: 'space-between',
});

const RoomLable = styled.div({
  maxWidth: 100,
  whiteSpace: 'nowrap',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
});

const Left = styled.div({
  display: 'flex',
  // display: 'grid',
  alignItems: 'center',
});

const Center = styled.div({
  textAlign: 'center',
});

const Right = styled.div({
  textAlign: 'right',
});

const LogoLink = styled.a({
  margin: '0 16px',
});

export const View: React.FC = ({ children }) => (
  <Toolbar>
    <Left>
      <LogoLink href="https://openvidu.io/" target="_blank">
        {/* <LogoImg alt="OpenVidu Logo" src="images/openvidu_logo.png" /> */}
        <Logo width={40} color="#fff" />
      </LogoLink>
      <RoomLable>{children}</RoomLable>
    </Left>
    <Center>
      <Button.IconButton>mic</Button.IconButton>
      <Button.IconButton>videocam</Button.IconButton>
      <Button.IconButton>screen_share</Button.IconButton>
      <Button.IconButton>fullscreen</Button.IconButton>
      <Button.IconButton>power_settings_new</Button.IconButton>
    </Center>
    <Right>
      <Button.IconButton>chat</Button.IconButton>
    </Right>
  </Toolbar>
);
