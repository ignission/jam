import React from 'react';

interface Props {
  width?: number | string;
}

const Logo: React.FC<Props> = ({ width }) => {
  return (
    <svg
      x="0px"
      y="0px"
      width={width ? width : '100%'}
      viewBox="0 0 220.9 122.2"
    >
      <g fill="currentColor">
        <path
          d="M28.1,85.3 M28.1,85.3h3.4L31.5,66h-3.3l0,0C12.6,66,0,78.6,0,94.1s12.6,28.1,28.1,28.1
		c15.5,0,28.1-12.6,28.1-28.1H37c0,4.9-4,8.8-8.8,8.8c-4.9,0-8.8-4-8.8-8.8S23.3,85.3,28.1,85.3"
        />
        <path d="M83.9,0H112l19.6,122.2h-19.4l-3.3-22.2H85.2l-3.3,22.2H64.3L83.9,0z M87.7,83.5h18.5L97,21.7L87.7,83.5z" />
        <rect x="36.9" y="0.2" width="19.4" height="94.3" />
        <path d="M181.1,86.8L194.2,0h26.7v122.2h-18.2V34.6l-13.3,87.7h-18.2L157,35.8v86.4h-16.8V0H167L181.1,86.8z" />
      </g>
    </svg>
  );
};

export default Logo;
