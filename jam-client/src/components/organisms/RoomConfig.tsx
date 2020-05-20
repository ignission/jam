import React, { useState } from 'react';
import styled from '@emotion/styled';
import { IconTextButton } from '../atoms/Button';

interface Props {
  roomName: string;
}

// const Config = styled.div({});

const Header = styled.div({
  textAlign: 'center',
});

const Body = styled.div({
  display: 'flex',
  '@media only screen and (max-width: 768px)': {
    flexDirection: 'column',
  },
});

const Column = styled.div({
  flex: '1 1 100%',
});

const RoomName = styled.h3({});

const Video = styled.video({
  width: '100%',
  background: '#ccc',
});

const Footer = styled.div({});

export const View: React.FC<Props> = ({ roomName }) => {
  return (
    <>
      <Header>Set up your room</Header>
      <Body>
        <Column>
          <RoomName>Session: {roomName}</RoomName>
          <Video />
          <IconTextButton iconName="camera_alt" hasBorder>
            Capture Avatar
          </IconTextButton>
        </Column>
        <Column>aaaa</Column>
      </Body>
      <Footer>
        <IconTextButton hasBorder>JOIN</IconTextButton>
      </Footer>
    </>
  );
};
