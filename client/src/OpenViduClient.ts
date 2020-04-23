import axios from 'axios';
import { WindowService } from './WindowService';

interface OpenViduClient {
  getToken: (sessionId: string) => Promise<String>;
}

export const OpenViduClientImpl = (
  baseUrl: string,
  serverSecret: string,
  windowService: WindowService
): OpenViduClient => ({
  getToken: (sessionId: string): Promise<string> =>
    createSession(baseUrl, serverSecret, windowService, sessionId).then((sid) =>
      createToken(baseUrl, serverSecret, sid)
    ),
});

const createSession = (
  baseUrl: string,
  serverSecret: string,
  windowService: WindowService,
  sessionId: any
) => {
  return new Promise((resolve, reject) => {
    var data = JSON.stringify({ customSessionId: sessionId });
    axios
      .post(baseUrl + '/api/sessions', data, {
        headers: {
          Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + serverSecret),
          'Content-Type': 'application/json',
        },
      })
      .then((response) => {
        console.log('CREATE SESION', response);
        resolve(response.data.id);
      })
      .catch((response) => {
        var error = Object.assign({}, response);
        if (error.response.status === 409) {
          resolve(sessionId);
        } else {
          console.log(error);
          console.warn(
            'No connection to OpenVidu Server. This may be a certificate error at ' +
              baseUrl
          );
          if (windowService.confirm(baseUrl)) {
            windowService.assign(baseUrl + '/accept-certificate');
          }
        }
      });
  });
};

const createToken = (
  baseUrl: string,
  serverSecret: string,
  sessionId: any
): Promise<string> => {
  return new Promise((resolve, reject) => {
    var data = JSON.stringify({ session: sessionId });
    axios
      .post(baseUrl + '/api/tokens', data, {
        headers: {
          Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + serverSecret),
          'Content-Type': 'application/json',
        },
      })
      .then((response) => {
        console.log('TOKEN', response);
        resolve(response.data.token);
      })
      .catch((error) => reject(error));
  });
};
