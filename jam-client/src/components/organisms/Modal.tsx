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
  background: '#fff',
  borderRadius: 8,
  zIndex: 1,
  boxShadow: '0 0 10px rgba(0,0,0,0.2)',
  overflow: 'hidden',
});

const ModalContents = styled.div({
  overflowY: 'auto',
  maxHeight: '100%',
  padding: 40,
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
            <ModalContents>{children}</ModalContents>
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
