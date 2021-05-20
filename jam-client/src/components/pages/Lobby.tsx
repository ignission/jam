import React, { useState, useEffect } from 'react';
import { Stage, Sprite, Text, useApp } from '@inlet/react-pixi';
import { TextStyle } from 'pixi.js';
import Sockette from 'sockette';
import { number } from 'yup/lib/locale';

interface UserProps {
  readonly width?: number;
  readonly height?: number;
  readonly fontSize?: number;
  readonly userName: string;
  onPositionChange: (x: number, y: number) => void;
}

const UserComponent: React.FC<UserProps> = ({
  width = 50,
  height = 50,
  fontSize = 16,
  userName,
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

interface LobbyProps {
  userName: string;
}

interface Position {
  x: number;
  y: number;
}

interface User {
  name: string;
  position: Position;
}

export const Lobby: React.FC<LobbyProps> = ({ userName }) => {
  const width = 50;
  const height = 50;
  const fontSize = 16;

  const [users, setUsers] = useState<User[]>([]);
  const ws = new Sockette('ws:/localhost:9001/ws/' + userName, {
    timeout: 10,
    maxAttempts: 10,
    onopen: (e) => {
      console.log('Connected!', e);
    },
    onmessage: (e) => {
      console.log('Received', e);
      const data = JSON.parse(e.data);
      if (data) setUsers(data.users);
    },
    onreconnect: (e) => {
      console.log('Reconnecting...', e);
    },
    onmaximum: (e) => console.log('Stop Attempting!', e),
    onclose: (e) => console.log('Closed!', e),
    onerror: (e) => console.log('Error!', e),
  });

  const onPositionChange = (x: number, y: number) => {
    ws.send(
      JSON.stringify({
        command: 'move',
        value: {
          x: x,
          y: y,
        },
      })
    );
  };

  return (
    <Stage options={{ backgroundColor: 0xeef1f5 }}>
      <UserComponent userName={userName} onPositionChange={onPositionChange} />
      {users.map((user: User, index: number) => {
        if (userName != user.name) {
          const x = user.position.x;
          const y = user.position.y;
          return (
            <>
              <Sprite
                image="images/favicon.ico"
                anchor={0.5}
                {...{ x, y, width, height }}
              />
              <Text
                text={user.name}
                x={user.position.x}
                y={user.position.y + 25}
                anchor={[0.5, 0]}
                style={new TextStyle({ fontSize })}
              />
            </>
          );
        }
      })}
    </Stage>
  );
};
