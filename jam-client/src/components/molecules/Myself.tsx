import React, { useState, useEffect } from 'react';
import { Sprite, Text, useApp } from '@inlet/react-pixi';
import { TextStyle } from 'pixi.js';
import { ChatBalloon } from './';

interface Props {
  readonly width?: number;
  readonly height?: number;
  readonly fontSize?: number;
  readonly userName: string;
  readonly message: string;
  isTyping: boolean;
  onPositionChange: (x: number, y: number) => void;
}

export const Myself: React.FC<Props> = ({
  width = 50,
  height = 50,
  fontSize = 16,
  userName,
  message,
  isTyping,
  onPositionChange,
}) => {
  const app = useApp();
  const [halfWidth, halfHeight] = [width / 2, height / 2];
  const [x, setX] = useState(100);
  const [y, setY] = useState(100);

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
      if (moveTo.x && !isTyping)
        setX((x) => {
          const newX = x + moveTo.x;
          if (newX - halfWidth < 0 || newX + halfHeight > app.screen.width)
            return x;

          onPositionChange(newX, y);
          return newX;
        });
      if (moveTo.y && !isTyping)
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
      {message != '' && (
        <ChatBalloon
          x={x - 25}
          y={y - 80}
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
