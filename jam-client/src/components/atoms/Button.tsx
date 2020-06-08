import React, { useState } from 'react';
import styled from '@emotion/styled';
/** @jsx jsx */
import { jsx, css } from '@emotion/core';

interface IconTextProps {
  iconName?: string;
  iconColor?: boolean;
  width?: string | number;
  variant?: variantType;
  color?: ButtonColor;
  p?: string | number;
  onClick?: () => void;
}

export enum variantType {
  contained = 'contained',
  outlined = 'outlined',
}

export enum ButtonColor {
  primary = 'primary',
  secondary = 'secondary',
  default = 'default',
}

export const CommonButtonStyled = css({
  background: 'transparent',
  border: 'transparent',
  padding: '8px 16px',
  cursor: 'pointer',
  fontSize: '1.4rem',
  display: 'flex',
  alignItems: 'center',
});

// const ColorStyle = (props: any) =>
//   css(
//     props.color === ButtonColor.primary
//       ? css({
//           color: '#fff',
//           borderColor: '#e48844',
//           backgroundColor: '#e48844',
//         })
//       : props.color === ButtonColor.secondary
//       ? css({})
//       : null,
//     {}
//   );

const ButtonStyled = styled.button(
  CommonButtonStyled,
  // ColorStyle,
  {
    display: 'inline-flex',
    // padding: '4px 8px',
    alignItems: 'center',
    borderRadius: 4,
  },
  (props: {
    iconColor?: boolean;
    width?: string | number;
    variant?: variantType;
    color?: ButtonColor;
    p?: string | number;
  }) => ({
    color: props.iconColor ? '#ff0000' : 'inherit',
    width: props.width ? props.width : 'auto',
    padding: props.p ? props.p : '8px 16px',
  })
);

const ContainedButton = styled.button(
  CommonButtonStyled,
  {
    background: '#ec9b5f',
    borderRadius: 3,
  },
  (props: {
    iconColor?: boolean;
    width?: string | number;
    color?: ButtonColor;
    p?: string | number;
  }) => ({
    // color: props.iconColor ? '#ff0000' : 'inherit',
    width: props.width ? props.width : 'auto',
    padding: props.p ? props.p : '8px 16px',
    color: props.color === ButtonColor.default ? '#666' : '#fff',
  })
);

const OutlinedButton = styled.button(
  CommonButtonStyled,
  {
    border: '1px solid #ec9b5f',
    borderRadius: 3,
  },
  (props: {
    iconColor?: boolean;
    width?: string | number;
    color?: ButtonColor;
    p?: string | number;
  }) => ({
    // color: props.iconColor ? '#ff0000' : 'inherit',
    width: props.width ? props.width : 'auto',
    border: '1px solid #666',
    color: '#666',
    padding: props.p ? props.p : '8px 16px',
  })
);

const IconTextButtonLabel = styled.span({
  marginLeft: 4,
});

const Contained: React.FC<IconTextProps> = (props) => {
  switch (props.variant) {
    case variantType.contained:
      return (
        <ContainedButton
          iconColor={props.iconColor}
          onClick={props.onClick}
          width={props.width}
          color={props.color}
          p={props.p}
        >
          {props.iconName ? (
            <i className="material-icons">{props.iconName}</i>
          ) : null}
          {props.children ? (
            <IconTextButtonLabel>{props.children}</IconTextButtonLabel>
          ) : null}
        </ContainedButton>
      );
      break;
    case variantType.outlined:
      return (
        <OutlinedButton
          iconColor={props.iconColor}
          onClick={props.onClick}
          width={props.width}
          color={props.color}
          p={props.p}
        >
          {props.iconName ? (
            <i className="material-icons">{props.iconName}</i>
          ) : null}
          {props.children ? (
            <IconTextButtonLabel>{props.children}</IconTextButtonLabel>
          ) : null}
        </OutlinedButton>
      );
      break;
    default:
      return (
        <ButtonStyled
          iconColor={props.iconColor}
          onClick={props.onClick}
          width={props.width}
          color={props.color}
          p={props.p}
        >
          {props.iconName ? (
            <i className="material-icons">{props.iconName}</i>
          ) : null}
          {props.children ? (
            <IconTextButtonLabel>{props.children}</IconTextButtonLabel>
          ) : null}
        </ButtonStyled>
      );
      break;
  }
};

export default Contained;
