package com.kakao.story
package s2graph
package model

import serialize.syntax.all._
import cats.syntax.either._
import com.kakao.story.serialize.{JsonDecoder, JsonEncoder}

class Identity(val value: String) extends AnyVal

case object Identity extends IdentityInstances {
  def apply(s: String): Identity = {
    assert(s.getBytes("UTF-8").size < 249, s"Identity($s)'s length over 249")
    assert(s.getBytes("UTF-8").size > 0, s"Identity not allow empty")
    new Identity(s)
  }

  implicit def fromString(s: String): Identity = apply(s)
}

trait IdentityInstances {
  implicit val identityEncoder = new JsonEncoder[Identity] {
    def toJson(a: Identity): String = a.value.toJson
  }

  implicit val identityDecoder = new JsonDecoder[Identity] {
    def jsonTo(a: String): Either[Throwable, Identity] = a.value.jsonTo[String].map(Identity.apply)
  }
}
