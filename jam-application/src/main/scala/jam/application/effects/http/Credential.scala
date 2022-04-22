package jam.application.effects.http

sealed trait Credential
case class BasicCredential(username: String, password: String) extends Credential
