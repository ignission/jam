import { StreamManager } from 'openvidu-browser';
import { Option, some, none } from 'fp-ts/es6/Option';

type UserType = 'remote' | 'local';

export interface User {
  name: string;
  connectionId: string;
  audioActive: boolean;
  videoActive: boolean;
  screenShareActive: boolean;
  streamManager: Option<StreamManager>;
  type: UserType;
  isAudioActive(): boolean;
  isVideoActive(): boolean;
  isScreenShareActive(): boolean;
  getConnectionId(): string;
  getName(): string;
  getStreamManager(): Option<StreamManager>;
  isLocal(): boolean;
  isRemote(): boolean;
  setAudioActive(isAudioActive: boolean): User;
  setVideoActive(isVideoActive: boolean): User;
  setScreenShareActive(isScreenShareActive: boolean): User;
  setStreamManager(streamManager: StreamManager): User;
  setConnectionId(connectionId: string): User;
  setName(name: string): User;
  setType(type: UserType): User;
}

export const User = (): User => {
  const self: User = {
    name: '',
    connectionId: '',
    audioActive: true,
    videoActive: true,
    screenShareActive: false,
    streamManager: none,
    type: 'local',
    isAudioActive: (): boolean => self.audioActive,
    isVideoActive: (): boolean => self.videoActive,
    isScreenShareActive: (): boolean => self.screenShareActive,
    getConnectionId: (): string => self.connectionId,
    getName: (): string => self.name,
    getStreamManager: (): Option<StreamManager> => self.streamManager,
    isLocal: (): boolean => self.type == 'local',
    isRemote: (): boolean => self.type == 'remote',
    setAudioActive: (flag: boolean): User => {
      return { ...self, audioActive: flag };
    },
    setVideoActive: (flag: boolean): User => {
      return { ...self, videoActive: flag };
    },
    setScreenShareActive: (flag: boolean): User => {
      return { ...self, screenShareActive: flag };
    },
    setStreamManager: (value: StreamManager): User => {
      return { ...self, streamManager: some(value) };
    },
    setConnectionId: (value: string): User => {
      return { ...self, connectionId: value };
    },
    setName: (value: string): User => {
      return { ...self, name: value };
    },
    setType: (value: UserType): User => {
      return { ...self, type: value };
    },
  };
  return self;
};
