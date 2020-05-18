import { StreamManager, Publisher, Stream, Connection } from 'openvidu-browser';
import {
  Option,
  some,
  none,
  map,
  getOrElse,
  fromNullable,
  mapNullable,
} from 'fp-ts/es6/Option';
import { pipe } from 'fp-ts/es6/pipeable';

export interface User {
  /**
   * The user name
   */
  readonly name: string;

  /**
   * The Connection ID that is publishing the stream
   */
  readonly connectionId: Option<string>;

  /**
   * Return `true` if audio track is active and `false` if audio track is muted
   */
  readonly audioActive: boolean;

  /**
   * Return `true` if video track is active and `false` if video track is muted
   */
  readonly videoActive: boolean;

  /**
   * StreamManager object ([[Publisher]] or [[Subscriber]])
   */
  readonly streamManager?: StreamManager;

  getConnectionId(): string;
  isLocal(): boolean;
  isRemote(): boolean;
  setAudioActive(isAudioActive: boolean): User;
  setVideoActive(isVideoActive: boolean): User;
  setStreamManager(streamManager: StreamManager): User;
  setConnectionId(connectionId: string): User;
  setName(name: string): User;
}

export const User = (): User => {
  const self: User = {
    name: 'OpenVidu',
    connectionId: none,
    audioActive: true,
    videoActive: true,
    streamManager: undefined,
    getConnectionId: (): string => {
      return pipe(
        fromNullable(self.streamManager),
        mapNullable((streamManager) => streamManager.stream),
        mapNullable((stream) => stream.connection),
        mapNullable((connection) => connection.connectionId),
        getOrElse(() => '')
      );
    },
    isLocal: (): boolean => !self.isRemote(),
    isRemote: (): boolean => {
      return pipe(
        fromNullable(self.streamManager),
        mapNullable((streamManager) => (streamManager as Publisher).remote),
        getOrElse(() => false as boolean)
      );
    },
    setAudioActive: (flag: boolean): User => {
      return { ...self, audioActive: flag };
    },
    setVideoActive: (flag: boolean): User => {
      return { ...self, videoActive: flag };
    },
    setStreamManager: (value: StreamManager): User => {
      return { ...self, streamManager: value };
    },
    setConnectionId: (value: string): User => {
      return { ...self, connectionId: some(value) };
    },
    setName: (value: string): User => {
      return { ...self, name: value };
    },
  };
  return self;
};
