import React, { useState } from 'react';
import { Stage, Sprite, useApp, useTick } from '@inlet/react-pixi';

const User: React.FC = () => {
  const app = useApp();
  const [x, setX] = useState(0);
  const [y, setY] = useState(0);

  useTick(() => {
    const mousePosition = app.renderer.plugins.interaction.mouse.global;
    setX(mousePosition.x);
    setY(mousePosition.y);
  });

  return <Sprite image="images/favicon.ico" x={x} y={y} />;
};

export const Lobby: React.FC = () => {
  return (
    <Stage>
      <User />
    </Stage>
  );
};
