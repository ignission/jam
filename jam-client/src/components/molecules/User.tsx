import React, { useState, useEffect } from 'react';
import { Sprite, Text, useApp, PixiComponent } from '@inlet/react-pixi';
import { TextStyle, Graphics } from 'pixi.js';

interface Props {
  readonly width?: number;
  readonly height?: number;
  readonly fontSize?: number;
  readonly userName: string;
  readonly message: string;
  onPositionChange: (x: number, y: number) => void;
}

interface ChatBalloonProps {
  x: number;
  y: number;
  width: number;
  height: number;
  color: number;
  fontSize: number;
  text: string;
}

interface BalloonProps {
  x: number;
  y: number;
  width: number;
  height: number;
  color: number;
}

const Balloon = PixiComponent<BalloonProps, Graphics>('Balloon', {
  create: () => new Graphics(),
  applyProps: (ins, _, props) => {
    ins.clear();
    ins.lineStyle(2, 0xff00ff, 1);
    ins.beginFill(0x650a5a, 0);
    ins.drawRoundedRect(props.x, props.y, props.width, props.height, 16);
    ins.endFill();
  },
});

export const ChatBalloon: React.FC<ChatBalloonProps> = ({
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

const User: React.FC<Props> = ({
  width = 50,
  height = 50,
  fontSize = 16,
  userName,
  message,
  onPositionChange,
}) => {
  const app = useApp();
  const [halfWidth, halfHeight] = [width / 2, height / 2];
  const [x, setX] = useState(halfWidth);
  const [y, setY] = useState(halfHeight);

  // useTick(() => {
  //   const mousePosition = app.renderer.plugins.interaction.mouse.global;
  //   setX(mousePosition.x);
  //   setY(mousePosition.y);
  // });

  useEffect(() => {
    function moveUser(e: KeyboardEvent) {
      const key = e.key.toLowerCase();
      const moveTo = { x: 0, y: 0 };
      switch (key) {
        case 'w':
        case 'k':
        case 'up':
        case 'arrowup':
          moveTo.y = -10;
          break;
        case 'a':
        case 'h':
        case 'left':
        case 'arrowleft':
          moveTo.x = -10;
          break;
        case 's':
        case 'j':
        case 'down':
        case 'arrowdown':
          moveTo.y = 10;
          break;
        case 'd':
        case 'l':
        case 'right':
        case 'arrowright':
          moveTo.x = 10;
          break;
        default:
          break;
      }
      if (moveTo.x === 0 && moveTo.y === 0) return;
      if (moveTo.x)
        setX((x) => {
          const newX = x + moveTo.x;
          if (newX - halfWidth < 0 || newX + halfHeight > app.screen.width)
            return x;

          onPositionChange(newX, y);
          return newX;
        });
      if (moveTo.y)
        setY((y) => {
          const newY = y + moveTo.y;
          if (
            newY - halfHeight < 0 ||
            newY + halfHeight + fontSize > app.screen.height
          )
            return y;

          onPositionChange(x, newY);
          return newY;
        });
    }
    document.addEventListener('keydown', moveUser);

    return () => {
      document.removeEventListener('keydown', moveUser);
    };
  });

  return (
    <>
      <ChatBalloon
        x={x - 25}
        y={y - 80}
        width={width + 100}
        height={height}
        color={0xfff}
        text={message}
        fontSize={fontSize}
      />
      <Sprite
        image="images/favicon.ico"
        anchor={0.5}
        {...{ x, y, width, height }}
      />
      <Text
        text={userName}
        x={x}
        y={y + 25}
        anchor={[0.5, 0]}
        style={new TextStyle({ fontSize })}
      />
    </>
  );
};

export default User;
