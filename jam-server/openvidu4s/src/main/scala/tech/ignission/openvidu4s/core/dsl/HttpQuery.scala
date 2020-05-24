package tech.ignission.openvidu4s.core.dsl

import tech.ignission.openvidu4s.core.Credentials

case class HttpQuery(
    path: String,
    credentials: Credentials,
    baseUrl: String
)
