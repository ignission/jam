import Axios from 'axios';

export interface Session {
  readonly id: string;
  readonly createdAt: number;
}

export interface APIClient {
  listSessions: () => Promise<ReadonlyArray<Session>>;
  generateToken: (sessionId: string) => Promise<string>;
}

export const APIClientOnAxios = (baseUrl: string): APIClient => ({
  listSessions: (): Promise<ReadonlyArray<Session>> =>
    new Promise((resolve, reject) => {
      Axios.get(baseUrl + '/rest/api/v1/sessions')
        .then((response) => {
          console.log('LIST SESSION');
          resolve(response.data.sessions);
        })
        .catch((error) => {
          console.log(error);
          reject(error);
        });
    }),
  generateToken: (sessionId: string): Promise<string> => {
    return new Promise((resolve, reject) => {
      Axios.post(baseUrl + '/rest/api/v1/tokens/' + sessionId, {})
        .then((response) => {
          console.log('GENERATE TOKEN');
          resolve(response.data.token);
        })
        .catch((error) => reject(error));
    });
  },
});

// });

// const createSession = (
//   baseUrl: string,
//   serverSecret: string,
//   windowService: WindowService,
//   sessionId: any
// ) => {
//   return new Promise((resolve, reject) => {
//     var data = JSON.stringify({ customSessionId: sessionId });
//     axios
//       .post(baseUrl + '/api/sessions', data, {
//         headers: {
//           Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + serverSecret),
//           'Content-Type': 'application/json',
//         },
//       })
//       .then((response) => {
//         console.log('CREATE SESION', response);
//         resolve(response.data.id);
//       })
//       .catch((response) => {
//         var error = Object.assign({}, response);
//         if (error.response.status === 409) {
//           resolve(sessionId);
//         } else {
//           console.log(error);
//           console.warn(
//             'No connection to OpenVidu Server. This may be a certificate error at ' +
//             baseUrl
//           );
//           if (windowService.confirm(baseUrl)) {
//             windowService.assign(baseUrl + '/accept-certificate');
//           }
//         }
//       });
//   });
// };

// const createToken = (
//   baseUrl: string,
//   serverSecret: string,
//   sessionId: any
// ): Promise<string> => {
//   return new Promise((resolve, reject) => {
//     var data = JSON.stringify({ session: sessionId });
//     axios
//       .post(baseUrl + '/api/tokens', data, {
//         headers: {
//           Authorization: 'Basic ' + btoa('OPENVIDUAPP:' + serverSecret),
//           'Content-Type': 'application/json',
//         },
//       })
//       .then((response) => {
//         console.log('TOKEN', response);
//         resolve(response.data.token);
//       })
//       .catch((error) => reject(error));
//   });
// };
