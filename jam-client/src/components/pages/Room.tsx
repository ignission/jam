import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import styled from '@emotion/styled';
import * as Toolber from '../organisms/Toolbar';

type PageProps = {} & RouteComponentProps<{ id: string }>;

const Grid = styled.div({
  display: 'grid',
  height: '100%',
  gridTemplateRows: '40px 1fr',
  gridTemplateAreas: `
  "Toolbar"
  "Content"
  `,
});

const Contents = styled.div({
  gridArea: 'Content',
});

export const Room: React.FC<PageProps> = (props) => {
  return (
    <Grid>
      <Toolber.View>{props.match.params.id}</Toolber.View>
      <Contents>Room: {props.match.params.id}</Contents>
    </Grid>
  );
};
