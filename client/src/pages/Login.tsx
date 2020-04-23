import React, { FormEvent, ChangeEvent } from 'react';

interface Props {
  userName: string;
  sessionId: string;
  joinSession: () => void;
  setUserName: (event: ChangeEvent<HTMLInputElement>) => void;
  setSessionId: (event: ChangeEvent<HTMLInputElement>) => void;
}

export const Login: React.FC<Props> = (props) => (
  <div id="join">
    <div id="img-div">
      <img
        src="images/openvidu_grey_bg_transp_cropped.png"
        alt="OpenVidu logo"
      />
    </div>
    <div id="join-dialog" className="jumbotron vertical-center">
      <h1> Join a video session </h1>
      <form className="form-group" onSubmit={props.joinSession}>
        <p>
          <label>Participant: </label>
          <input
            className="form-control"
            type="text"
            id="userName"
            value={props.userName}
            onChange={props.setUserName}
            required
          />
        </p>
        <p>
          <label> Session: </label>
          <input
            className="form-control"
            type="text"
            id="sessionId"
            value={props.sessionId}
            onChange={props.setSessionId}
            required
          />
        </p>
        <p className="text-center">
          <input
            className="btn btn-lg btn-success"
            name="commit"
            type="submit"
            value="JOIN"
          />
        </p>
      </form>
    </div>
  </div>
);
