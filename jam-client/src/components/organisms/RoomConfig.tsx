import React, { useState } from 'react';
import styled from '@emotion/styled';
import Button, { variantType } from '../atoms/Button';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';

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

const Footer = styled.div({
  display: 'flex',
  justifyContent: 'center',
});

const ToggleIconButton = ({ text }) => {
  const [clickState, setClickState] = useState(false);
  return (
    <Button
      iconName={text}
      onClick={() => setClickState(!clickState)}
      iconColor={clickState}
    />
  );
};

export const View: React.FC<Props> = ({ roomName, onClick }) => {
  const MicList = [
    {
      value: 'MIC 1',
      label: 'MIC 1',
    },
    {
      value: 'MIC 2',
      label: 'MIC 2',
    },
    {
      value: 'MIC 3',
      label: 'MIC 3',
    },
  ];
  const VideoList = [
    {
      value: 'VIDEO 1',
      label: 'VIDEO 1',
    },
    {
      value: 'VIDEO 2',
      label: 'VIDEO 2',
    },
    {
      value: 'VIDEO 3',
      label: 'VIDEO 3',
    },
  ];
  const [stateMicList, setStateMicList] = React.useState(MicList[0].label);
  const [stateVideoList, setStateVideoList] = React.useState(
    VideoList[0].label
  );

  const MicChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStateMicList(event.target.value);
  };
  const VideoChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStateVideoList(event.target.value);
  };

  return (
    <>
      <Header>Set up your room</Header>
      <Body>
        <Column>
          <RoomName>Session: {roomName}</RoomName>
          <Video />
          <Button iconName="camera_alt" variant={variantType.outlined}>
            Capture Avatar
          </Button>
        </Column>
        <Column>
          <FromSection>
            <ToggleIconButton text="mic" />
            <TextField
              select
              value={stateMicList}
              onChange={MicChange}
              fullWidth
            >
              {MicList.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </TextField>
          </FromSection>
          <FromSection>
            <ToggleIconButton text="videocam" />
            <TextField
              select
              value={stateVideoList}
              onChange={VideoChange}
              fullWidth
            >
              {VideoList.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </TextField>
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
        <Button variant={variantType.contained} onClick={onClick}>
          JOIN
        </Button>
      </Footer>
    </>
  );
};
