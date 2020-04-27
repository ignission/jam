import React from 'react';
import { Container } from 'components/atoms/Container';
import { EnterRoomForm } from 'components/molecules/EnterRoomForm';

import './Home.css';

const version = require('../../package.json').version;

const Home: React.FC = () => {
  return (
    <div className="section1">
      <header id="header">
        <a href="https://openvidu.io/" target="_blank">
          <img
            id="header_img"
            alt="OpenVidu Logo"
            src="images/openvidu_logo.png"
          />
        </a>
        <div className="ovVersion">
          <span>{version}</span>
        </div>
      </header>
      <Container>
        <div className="ovInfo">
          <img
            className="ovLogo"
            alt="OpenVidu Logo"
            src="images/openvidu_vert_white_bg_trans_cropped.png"
          />
          <h4>Videoconference rooms in one click</h4>
        </div>
        <div className="formContainer">
          <EnterRoomForm />
        </div>
      </Container>

      <footer className="footer">
        <span>
          Photo by
          <a
            rel="noopener noreferrer"
            target="_blank"
            href="https://unsplash.com/@danielleone?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText"
          >
            Daniel Leone
          </a>
          on
          <a
            href="https://unsplash.com/s/photos/mountain?utm_source=unsplash&utm_medium=referral&utm_content=creditCopyText"
            target="_blank"
          >
            Unsplash
          </a>
        </span>
      </footer>
    </div>
  );
};

export default Home;
