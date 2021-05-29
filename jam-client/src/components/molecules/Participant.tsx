import React from 'react';
import { TextStyle } from 'pixi.js';
import { Sprite, Text } from '@inlet/react-pixi';
import { ChatBalloon } from '.';
import { Position } from 'models';

interface Props {
  width: number;
  height: number;
  fontSize: number;
  position: Position;
  name: string;
  message: string;
}

export const Participant: React.FC<Props> = ({
  width,
  height,
  fontSize,
  position,
  name,
  message,
}) => {
  return (
    <>
      {message && message != '' && (
        <ChatBalloon
          x={position.x - 25}
          y={position.y - 80}
          width={width + 100}
          height={height}
          color={0xfff}
          text={message}
          fontSize={fontSize}
        />
      )}
      <Sprite
        image="images/favicon.ico"
        anchor={0.5}
        x={position.x}
        y={position.y}
        {...{ width, height }}
      />
      <Text
        text={name}
        x={position.x}
        y={position.y + 25}
        anchor={[0.5, 0]}
        style={new TextStyle({ fontSize })}
      />
    </>
  );
};
