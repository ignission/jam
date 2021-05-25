import React, { useState, useEffect, useRef } from 'react';
import { Stage, Sprite, Text } from '@inlet/react-pixi';
import { TextStyle } from 'pixi.js';
import Sockette from 'sockette';
import * as UserComponent from '../molecules/User';
import { Position } from 'models';
import { ChatBalloon } from '../molecules/User';

interface Props {
  userName: string;
}

interface User {
  name: string;
  position: Position;
  message: string;
}
const User = (name: string) => ({
  name: name,
  position: { x: 0, y: 0 },
  message: '',
});

export const Lobby: React.FC<Props> = ({ userName }) => {
  const width = 50;
  const height = 50;
  const fontSize = 16;

  const [users, setUsers] = useState<User[]>([]);
  const [message, setMessage] = useState<string>('');
  const wsRef = useRef<Sockette>();

  useEffect(() => {
    console.log('Connecting..');
    wsRef.current = new Sockette(
      WS_URL + '/ws/lobby?user_name=' + userName,
      {
        timeout: 10,
        maxAttempts: 10,
        onopen: (e) => {
          console.log('Connected!', e);
        },
        onmessage: (e) => {
          console.log('Received', e.data);
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
              case 'chat':
                setUsers((users) => {
                  const filtered = users.filter(
                    (user) => user.name != data.userName
                  );
                  const current = users.find(
                    (user) => user.name == data.userName
                  );
                  if (current) {
                    const updated = {
                      name: current.name,
                      position: current.position,
                      message: data.message,
                    };
                    filtered.push(updated);
                  }
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
          command: 'move',
          userName: userName,
          position: {
            x: x,
            y: y,
          },
        })
      );
  };

  const handleChange = (e: any) => {
    setMessage(e.target.value)
    if (wsRef.current)
      wsRef.current.send(
        JSON.stringify({
          command: 'chat',
          userName: userName,
          message: e.target.value,
        })
      );
  };

  return (
    <>
      <Stage options={{ backgroundColor: 0xeef1f5 }}>
        <UserComponent.default
          userName={userName}
          message={message}
          onPositionChange={onPositionChange}
        />
        {users.map((user: User) => {
          if (userName != user.name) {
            const x = user.position.x;
            const y = user.position.y;
            const isRenderingChatBalloon = (user.message != null && user.message != '') ? true : false;
            return (
              <>
                {isRenderingChatBalloon &&
                  <ChatBalloon
                    x={x - 25}
                    y={y - 80}
                    width={width + 100}
                    height={height}
                    color={0xfff}
                    text={user.message}
                    fontSize={fontSize}
                  />
                }
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
      <form>
        <label>
          Chat Message:
          <input type="text" onChange={handleChange} />
        </label>
      </form>
    </>
  );
};
