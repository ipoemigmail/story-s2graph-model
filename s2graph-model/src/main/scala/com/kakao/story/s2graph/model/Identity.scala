package com.kakao.story
package s2graph
package model

import serialize.syntax.all._
import cats.syntax.either._
import serialize.{JsonDecoder, JsonEncoder}
import cats.syntax.option._

sealed abstract class Identity { self =>
  def getString: Option[String] = self match {
    case StringIdentity(d) => d.some
    case _                 => none
  }

  def getLong: Option[Long] = self match {
    case LongIdentity(d) => d.some
    case _               => none
  }
}
final case class LongIdentity private (value: Long) extends Identity
final case class StringIdentity private (value: String) extends Identity

case object Identity extends IdentityInstances {
  def apply(s: String): Either[Throwable, Identity] = {
    if (s.getBytes("UTF-8").size > 249) new Throwable(s"Identity($s)'s length over 249").asLeft
    else if (s.getBytes("UTF-8").size < 1) new Throwable(s"Identity not allow empty").asLeft
    else StringIdentity(s).asRight
  }

  private[model] def unsafeApply(s: String): Identity = StringIdentity(s)

  def apply(l: Long): Either[Throwable, Identity] = LongIdentity(l).asRight
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
      a.jsonTo[Long].flatMap(Identity.apply) orElse a.jsonTo[String].flatMap(Identity.apply)
  }
}
