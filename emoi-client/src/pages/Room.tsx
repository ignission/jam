import React from 'react';
import { RouteComponentProps } from 'react-router-dom';

type PageProps = {} & RouteComponentProps<{ id: string }>;

export const Room: React.FC<PageProps> = (props) => {
  return <div>Room: {props.match.params.id}</div>;
};
