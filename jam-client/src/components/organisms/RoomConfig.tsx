import React, { useState } from 'react';
import styled from '@emotion/styled';
import * as Button from '../atoms/Button';

interface Props {
  roomName: string;
  onClick: () => void;
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

const FromSection = styled.div({
  display: 'flex',
  margin: '16px 0',
});

const Footer = styled.div({});

const ToggleIconButton = ({ text }) => {
  const [clickState, setClickState] = useState(false);
  return (
    <Button.Contained
      iconName={text}
      onClick={() => setClickState(!clickState)}
      iconColor={clickState}
      hasBorder
    />
  );
};

export const View: React.FC<Props> = ({ roomName, onClick }) => {
  return (
    <>
      <Header>Set up your room</Header>
      <Body>
        <Column>
          <RoomName>Session: {roomName}</RoomName>
          <Video />
          <Button.Contained iconName="camera_alt" hasBorder>
            Capture Avatar
          </Button.Contained>
        </Column>
        <Column>
          <FromSection>
            <ToggleIconButton text="mic" />
            <select name="">
              <option value="a">a</option>
              <option value="b">b</option>
              <option value="c">c</option>
            </select>
          </FromSection>
          <FromSection>
            <ToggleIconButton text="videocam" />
            <select name="">
              <option value="a">a</option>
              <option value="b">b</option>
              <option value="c">c</option>
            </select>
          </FromSection>
          <FromSection>
            <ToggleIconButton text="screen_share" />
            <input type="text" />
          </FromSection>
          <FromSection>
            <ToggleIconButton text="person" />
            <input type="text" />
          </FromSection>
        </Column>
      </Body>
      <Footer>
        <Button.Contained hasBorder width="100%" onClick={onClick}>
          JOIN
        </Button.Contained>
      </Footer>
    </>
  );
};
