# JAM

![Build](https://github.com/ignission/jam/workflows/Build/badge.svg)
![Http Server](https://github.com/ignission/jam/workflows/Http%20Server/badge.svg)
![Websocket Server](https://github.com/ignission/jam/workflows/Websocket%20Server/badge.svg)

Simplicity and Freedom of Space Video Chat Party App with OpenVidu.

## Getting started

    make up

Try to access `http://localhost:8855`

## Development

### Requirements

- Docker > 19.03.0
- Docker Compose > 1.25.4
- Node.js > 12.16.2
- Yarn > 1.22.4
- Java > 1.8
- sbt

### First step

    make init

### Server + Client

    make dev

- server: http://localhost:8855
- client: http://localhost:3000

### Server

    cd jam-server
    make dev

Next, `http://localhost:8855` done.

#### NOTE

You must run `sbt format` and `sbt fix` before committing.
If you need hot-reload feature, run the following command instead of `sbt run`

    sbt ~reStart

### Client

```shell
    docker-compose up -d # optional: if you want to watch logs, `docker-compose up`
    cd jam-client
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
