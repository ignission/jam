import React, { useState } from 'react';
import styled from '@emotion/styled';
import * as Button from '../atoms/Button';

const Skin = styled.div({
  position: 'absolute',
  background: '#000',
  opacity: '0.4',
  top: 0,
  right: 0,
  bottom: 0,
  left: 0,
});

const Modal = styled.div({
  position: 'absolute',
  top: 40,
  right: 40,
  bottom: 40,
  left: 40,
  padding: 40,
  overflowY: 'auto',
  background: '#fff',
  borderRadius: 8,
  zIndex: 1,
  boxShadow: '0 0 10px rgba(0,0,0,0.2)',
});

const Header = styled.div({
  textAlign: 'center',
});

const Close = styled.div({
  position: 'absolute',
  top: 0,
  right: 0,
});

export const View: React.FC = ({ children }) => {
  const [showModalState, setShowModalState] = useState(true);
  return (
    <>
      {showModalState ? (
        <>
          <Modal>
            <Header>Set up your room</Header>
            {children}
            <Close>
              <Button.IconButton
                onClick={() => setShowModalState(!showModalState)}
              >
                close
              </Button.IconButton>
            </Close>
          </Modal>
          <Skin />
        </>
      ) : null}
    </>
  );
};
