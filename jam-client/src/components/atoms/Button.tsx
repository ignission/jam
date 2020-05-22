import React, { useState } from 'react';
import styled from '@emotion/styled';

interface IconTextProps {
  iconName?: string;
  hasBorder?: boolean;
  iconColor?: boolean;
  width?: string | number;
  onClick?: () => void;
}

export const CommonButtonStyled = {
  background: 'transparent',
  border: 'transparent',
  padding: 0,
  cursor: 'pointer',
};

const ButtonStyled = styled.button(
  CommonButtonStyled,
  {
    display: 'inline-flex',
    padding: '4px 8px',
    alignItems: 'center',
    borderRadius: 4,
  },
  (props: {
    hasBorder?: boolean;
    iconColor?: boolean;
    width?: string | number;
  }) => ({
    border: props.hasBorder ? '1px solid #ddd' : 'none',
    color: props.iconColor ? '#ff0000' : 'inherit',
    width: props.width ? props.width : 'auto',
  })
);

const IconTextButtonLabel = styled.span({
  marginLeft: 4,
});

export const Contained: React.FC<IconTextProps> = ({
  iconName,
  hasBorder,
  iconColor,
  onClick,
  width,
  children,
}) => {
  return (
    <ButtonStyled
      hasBorder={hasBorder}
      iconColor={iconColor}
      onClick={onClick}
      width={width}
    >
      {iconName ? <i className="material-icons">{iconName}</i> : null}
      {children ? <IconTextButtonLabel>{children}</IconTextButtonLabel> : null}
    </ButtonStyled>
  );
};
