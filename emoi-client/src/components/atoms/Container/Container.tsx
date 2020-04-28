import React from 'react';
import styled from '@emotion/styled';

const ContainerStyled = styled.div({
  width: '100%',
  padding: '100px 0',
  position: 'absolute',
  top: '40%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
});

export const Container: React.FC = ({ children }) => (
  <ContainerStyled>{children}</ContainerStyled>
);
