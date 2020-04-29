# Emoi Chat
![](https://github.com/ignission/emoi-chat/workflows/Build/badge.svg)

A simple video chat party app with OpenVidu that brings you a freedom space

## Development

### Requirements

- Docker > 19.03.0
- Docker Compose > 1.25.4
- Node.js > 12.16.2
- Yarn > 1.22.4
- Java > 1.8
- sbt

### Server
    cd emoi-server
    sbt run

After that, `http://localhost:8855` is available.

#### NOTE
You must run `sbt scalafmtCheck test:scalafmtCheck scalafmtSbtCheck` before commit.
If you need hot-reload feature, run folloing command instead of `sbt run`
    sbt ~reStart


### Client
```shell
    docker-compose up -d # optional: if you want to watch logs, `docker-compose up`
    cd emoi-client
    yarn
    yarn hot
```

Then, you can access `http://localhost:3000`


#### NOTE

If an error occurs with `ERR_CERT_AUTHORITY_INVALID` in your browser's console,
you need to access `https://localhost:4443` only once.
Then, click `Proceed to localhost (unsafe)`.

##### Scala with VSCode
- `Settings` -> `Text Editor` -> `Formatting` -> Check `Format on Save`


## Contributers

- [iwa](https://github.com/mananyuki)
- [Makky](https://github.com/makotofukuda)
- [Yudai Ogawa](https://github.com/yudaiogawa)
