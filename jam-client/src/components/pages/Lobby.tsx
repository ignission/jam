import React, { useState, useEffect, useRef } from 'react';
import { Stage, Sprite, Text } from '@inlet/react-pixi';
import { TextStyle } from 'pixi.js';
import Sockette from 'sockette';
import * as UserComponent from '../molecules/User';

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
const User = (name: string) => ({
  name: name,
  position: { x: 0, y: 0 },
});

export const Lobby: React.FC<LobbyProps> = ({ userName }) => {
  const width = 50;
  const height = 50;
  const fontSize = 16;

  const [users, setUsers] = useState<User[]>([]);
  const wsRef = useRef<Sockette>();

  useEffect(() => {
    console.log('Connectinng..');
    wsRef.current = new Sockette(
      'ws:/localhost:9001/ws/lobby?user_name=' + userName,
      {
        timeout: 10,
        maxAttempts: 10,
        onopen: (e) => {
          console.log('Connected!', e);
        },
        onmessage: (e) => {
          console.log('Received', e);
          const data = JSON.parse(e.data);
          if (data && data.command) {
            switch (data.command) {
              case 'join':
                setUsers(data.users);
                break;
              case 'move':
                setUsers((users) => {
                  const filtered = users.filter(
                    (user) => user.name != data.user.name
                  );
                  filtered.push(data.user);
                  return filtered;
                });
                break;
              case 'leave':
                setUsers((users) => {
                  const filtered = users.filter(
                    (user) => user.name != data.user.name
                  );
                  return filtered;
                });
                break;
            }
          }
        },
        onreconnect: (e) => {
          console.log('Reconnecting...', e);
        },
        onmaximum: (e) => console.log('Stop Attempting!', e),
        onclose: (e) => console.log('Closed!', e),
        onerror: (e) => console.log('Error!', e),
      }
    );
    return () => {
      console.log('Disconnecting..');
      if (wsRef.current) wsRef.current.close();
    };
  }, []);

  const onPositionChange = (x: number, y: number) => {
    if (wsRef.current)
      wsRef.current.send(
        JSON.stringify({
          userName: userName,
          position: {
            x: x,
            y: y,
          },
        })
      );
  };

  return (
    <Stage options={{ backgroundColor: 0xeef1f5 }}>
      <UserComponent.default
        userName={userName}
        onPositionChange={onPositionChange}
      />
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
