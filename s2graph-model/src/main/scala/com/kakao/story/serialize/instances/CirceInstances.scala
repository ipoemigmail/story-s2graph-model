package com.kakao.story
package serialize
package instances

import java.util.UUID

import cats.implicits._
import io.circe.Decoder.Result
import io.circe._
import io.circe.parser.decode

trait CirceInstances {
  implicit def fromJsonEncoderToCirceEncoder[A](implicit J: JsonEncoder[A]) = new Encoder[A] {
    def apply(a: A): Json = decode[Json](J.toJson(a)).right.get
  }

  implicit def fromJsonEncoderToCirceKeyEncoder[A](implicit J: JsonEncoder[A]) = new KeyEncoder[A] {
    def apply(key: A): String =
      (decode[String](J.toJson(key)) orElse
        decode[UUID](J.toJson(key)).map(_.toString) orElse
        decode[Short](J.toJson(key)).map(_.toString) orElse
        decode[Int](J.toJson(key)).map(_.toString) orElse
        decode[Byte](J.toJson(key)).map(_.toString) orElse
        decode[Long](J.toJson(key)).map(_.toString) orElse
        key.toString.asRight[Error]).right.get
  }

  implicit def fromJsonDecoderToCirceDecoder[A](implicit J: JsonDecoder[A]) = new Decoder[A] {
    def apply(c: HCursor): Result[A] =
      J.jsonTo(c.value.toString).leftMap(th => DecodingFailure(th.getMessage, c.history))
  }

  /*
  implicit def fromJsonDecoderToCirceKeyDecoder[A](implicit J: JsonDecoder[A]) = new KeyDecoder[A] {
    def apply(key: String): Option[A] = J.jsonTo(key).toOption
  }
   */
  implicit def fromJsonDecoderToCirceKeyDecoder[A](implicit J: JsonDecoder[A]) = new KeyDecoder[A] {
    def apply(key: String): Option[A] = J.jsonTo("\"" + key + "\"").toOption
  }
}
