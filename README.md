# Emoi Chat

A simple video chat party app with OpenVidu that brings you a freedom space

## Development

### Requirements

- Docker > 19.03.0
- Docker Compose > 1.25.5
- Node.js > 12.16.2
- Yarn > 1.22.4

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
