import React, { useState } from 'react';
import styled from '@emotion/styled';

interface Props {
  onClick?: () => void;
}

export const IconButtonStyled = styled.button(
  {
    // color: 'inherit',
    background: 'transparent',
    border: 'transparent',
    padding: 0,
    minWidth: 0,
    width: 40,
    height: 40,
    flexShrink: 0,
    cursor: 'pointer',
    borderRadius: '50%',
  },
  (props: { iconColor?: boolean }) => ({
    color: props.iconColor ? '#ff0000' : 'inherit',
  })
);

export const IconButton: React.FC<Props> = ({ onClick, children }) => {
  // const [clickState, setClickState] = useState(false);
  return (
    <IconButtonStyled
      onClick={onClick}
      // iconColor={clickState}
    >
      <i className="material-icons">{children}</i>
    </IconButtonStyled>
  );
};
