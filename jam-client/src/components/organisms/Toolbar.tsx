import React, { useState } from 'react';
import styled from '@emotion/styled';
import Button, { variantType, ButtonColor } from '../atoms/Button';
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

const ToggleIconButton = ({ text }) => {
  const [clickState, setClickState] = useState(false);
  return (
    <Button
      iconName={text}
      onClick={() => setClickState(!clickState)}
      iconColor={clickState}
      p="8px"
    />
  );
};

export const View: React.FC = ({ children }) => {
  return (
    <Toolbar>
      <Left>
        <LogoLink href="/" target="_blank">
          <Logo width={40} color="#fff" />
        </LogoLink>
        <RoomLable>{children}</RoomLable>
      </Left>
      <Center>
        <ToggleIconButton text="mic" />
        <ToggleIconButton text="videocam" />
        <ToggleIconButton text="screen_share" />
        <ToggleIconButton text="fullscreen" />
        <ToggleIconButton text="power_settings_new" />
      </Center>
      <Right>
        <Button iconName="chat" />
      </Right>
    </Toolbar>
  );
};
