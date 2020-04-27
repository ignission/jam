import React from 'react';
import './Container.css';

export const Container: React.FC = ({ children }) => (
  <div className="container">{children}</div>
);
