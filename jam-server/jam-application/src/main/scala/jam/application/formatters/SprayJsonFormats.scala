package jam.application.formatters

import spray.json._

import jam.domains.Id

object SprayJsonFormats {

  implicit def idFormat[A](): JsonFormat[Id[A]] =
    new JsonFormat[Id[A]] {

      override def read(json: JsValue): Id[A] =
        json match {
          case JsNumber(idValue) =>
            Id(idValue.toLong)
          case _ =>
            throw DeserializationException(s"Expected id to be a number. Input: $json")
        }

      override def write(obj: Id[A]): JsValue =
        JsNumber(obj.value)
    }
}
