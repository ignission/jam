export interface WindowService {
  confirm: (url: string) => boolean;
  assign: (url: string) => void;
}

export const WindowServiceImpl = (window: Window): WindowService => ({
  confirm: (url: string): boolean =>
    window.confirm(
      'No connection to OpenVidu Server. This may be a certificate error at "' +
        url +
        '"\n\nClick OK to navigate and accept it. ' +
        'If no certificate warning is shown, then check that your OpenVidu Server is up and running at "' +
        url +
        '"'
    ),
  assign: (url: string): void => window.location.assign(url),
});
