import React from 'react';
import './Home.css';
import { Container } from 'components/atoms/Container';

const Home: React.FC = () => {
  return (
    <div className="section1">
      {/* <mat-toolbar id="header">
		<a href="https://openvidu.io/" target="_blank">
			<img id="header_img" alt="OpenVidu Logo" src="assets/images/openvidu_logo.png" />
		</a>
		<div className="ovVersion">
			<span>{{ version }}</span>
		</div>
	</mat-toolbar> */}
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
          <div className="roomError">
            Room name is <strong>required</strong>
          </div>
          <div className="roomError">
            Room name is <strong>too short!</strong>
          </div>
          <form>
            <div className="joinForm">
              <input
                // matInput
                className="inputForm"
                type="text"
                // autocomplete="off"
              />
              <button type="submit" className="joinButton">
                JOIN
              </button>
            </div>
          </form>
        </div>
      </Container>

      {/* <mat-toolbar className="footer">
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
      </mat-toolbar> */}
    </div>
  );
};

export default Home;
