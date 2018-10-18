package com.kakao.story
package s2graph
package model

import serialize.syntax.all._
import cats.syntax.either._
import serialize.{JsonDecoder, JsonEncoder}

trait Identity
case class LongIdentity(value: Long) extends Identity
case class StringIdentity(value: String) extends Identity

case object Identity extends IdentityInstances {
  def apply(s: String): Identity = {
    assert(s.getBytes("UTF-8").size < 249, s"Identity($s)'s length over 249")
    assert(s.getBytes("UTF-8").size > 0, s"Identity not allow empty")
    StringIdentity(s)
  }

  def apply(l: Long): Identity = LongIdentity(l)

  implicit def fromString(s: String): Identity = apply(s)
}

trait IdentityInstances {
  implicit val identityEncoder = new JsonEncoder[Identity] {
    def toJson(a: Identity): String = a.value match {
      case LongIdentity(v)   => v.toJson
      case StringIdentity(v) => v.toJson
    }
  }

  implicit val identityDecoder = new JsonDecoder[Identity] {
    def jsonTo(a: String): Either[Throwable, Identity] =
      a.jsonTo[Long].map(Identity.apply) orElse a.jsonTo[String].map(Identity.apply)
  }
}
