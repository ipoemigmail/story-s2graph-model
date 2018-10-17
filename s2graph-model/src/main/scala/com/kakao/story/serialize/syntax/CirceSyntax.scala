package com.kakao.story
package serialize
package syntax

import io.circe._
import io.circe.parser._

trait CirceSyntax {
  implicit class CirceEncoderOps[A](a: A)(implicit E: Encoder[A]) {
    def toJson: String = Printer.spaces2.copy(dropNullValues = true).pretty(E(a))
  }

  implicit class CirceDecoderOps(s: String) {
    def jsonTo[A](implicit D: Decoder[A]): Either[Throwable, A] = decode[A](s)
  }
}
