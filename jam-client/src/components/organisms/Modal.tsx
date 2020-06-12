import React, { useState } from 'react';
import styled from '@emotion/styled';
import Button from '../atoms/Button';

interface Props {
  onClick: () => void;
}

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

export const View: React.FC<Props> = ({ onClick, children }) => {
  return (
    <>
      <Modal>
        <ModalContents>{children}</ModalContents>
        <Close>
          <Button iconName="close" onClick={onClick} />
        </Close>
      </Modal>
      <Skin />
    </>
  );
};
