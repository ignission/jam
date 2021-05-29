import React from 'react';
import { TextStyle } from 'pixi.js';
import { Text } from '@inlet/react-pixi';
import { Balloon } from 'components/atoms';

interface Props {
  x: number;
  y: number;
  width: number;
  height: number;
  color: number;
  fontSize: number;
  text: string;
}

export const ChatBalloon: React.FC<Props> = ({
  x,
  y,
  width,
  height,
  text,
  fontSize,
}) => (
  <>
    <Balloon x={x} y={y} width={width} height={height} color={0xfff} />
    <Text
      text={text}
      x={x + 40}
      y={y + 20}
      anchor={[0.5, 0]}
      style={new TextStyle({ fontSize })}
    />
  </>
);
