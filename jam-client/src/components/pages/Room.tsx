import React, { useState } from 'react';
import { RouteComponentProps, useHistory } from 'react-router-dom';
import styled from '@emotion/styled';
import * as Toolber from '../organisms/Toolbar';
import * as Modal from '../organisms/Modal';
import * as Config from '../organisms/RoomConfig';

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
  const [showModalState, setShowModalState] = useState(true);
  let history = useHistory();
  const ToggleShowModal = () => {
    setShowModalState(!showModalState);
    history.push('/');
  };
  const JoinRoom = () => {
    setShowModalState(!showModalState);
  };
  return (
    <Grid>
      {showModalState ? (
        <Modal.View onClick={ToggleShowModal}>
          <Config.View roomName={props.match.params.id} onClick={JoinRoom} />
        </Modal.View>
      ) : (
        <>
          <Toolber.View>{props.match.params.id}</Toolber.View>
          <Contents>Room: {props.match.params.id}</Contents>
        </>
      )}
    </Grid>
  );
};
