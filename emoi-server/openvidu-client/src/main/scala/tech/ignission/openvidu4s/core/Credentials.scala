package tech.ignission.openvidu4s.core

sealed trait Credentials
case class Basic(username: String, password: String) extends Credentials
