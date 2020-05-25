import React, { useState } from 'react';
import styled from '@emotion/styled';

const Base = styled.div({
  background: '#af62a4',
  height: '100%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
});

const Box = styled.div({
  width: '100%',
  height: '100%',
  padding: 80,
});

const Contents = styled.div({
  background: '#fff',
  height: '100%',
});

const View: React.FC = () => {
  return (
    <Base>
      <Box>
        <Contents>aaa</Contents>
      </Box>
    </Base>
  );
};

export default View;
