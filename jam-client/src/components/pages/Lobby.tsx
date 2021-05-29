import React, { useState, useEffect, useRef } from 'react';
import { Stage } from '@inlet/react-pixi';
import Sockette from 'sockette';
import { Myself, Participant } from 'components/molecules';
import { Position } from 'models';

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
  const [users, setUsers] = useState<User[]>([]);
  const [message, setMessage] = useState<string>('');
  const [isTyping, setIsTyping] = useState<boolean>(false);
  const wsRef = useRef<Sockette>();
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    console.log('Connecting..');
    wsRef.current = new Sockette(WS_URL + '/ws/lobby?user_name=' + userName, {
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
    });

    function keyDown(e: KeyboardEvent) {
      const key = e.key.toLowerCase();
      switch (key) {
        case 'enter':
          if (inputRef.current) {
            inputRef.current.value = '';
            inputRef.current.focus();
          }
        case 'escape':
          if (inputRef.current) inputRef.current.blur();
        default:
          break;
      }
    }

    document.addEventListener('keydown', keyDown);

    return () => {
      console.log('Disconnecting..');
      if (wsRef.current) wsRef.current.close();

      document.removeEventListener('keydown', keyDown);
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
    setMessage(e.target.value);
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
        <Myself
          userName={userName}
          message={message}
          isTyping={isTyping}
          onPositionChange={onPositionChange}
        />
        {users.map((user: User) => {
          if (userName != user.name) {
            return (
              <Participant
                width={50}
                height={50}
                fontSize={16}
                name={user.name}
                position={user.position}
                message={user.message}
              />
            );
          }
        })}
      </Stage>
      <div>
        <label>
          Chat Message:
          <input
            type="text"
            ref={inputRef}
            onChange={handleChange}
            onFocus={() => setIsTyping(true)}
            onBlur={() => setIsTyping(false)}
          />
        </label>
      </div>
    </>
  );
};
