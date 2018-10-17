package com.kakao.story
package s2graph
package model

import serialize.{JsonDecoder, JsonEncoder}
import serialize.syntax.all._
import cats.data._, cats.implicits._

sealed trait Value
case class DoubleValue(value: Double) extends Value
case class StringValue(value: String) extends Value
case class IntValue(value: Int) extends Value

object Value extends ValueInstances {
  def apply(value: Double): Value = DoubleValue(value)
  def apply(value: String): Value = StringValue(value)
  def apply(value: Int): Value = IntValue(value)
}

trait ValueInstances {
  implicit val valueEncoder = new JsonEncoder[Value] {
    def toJson(a: Value): String = a match {
      case DoubleValue(d) => d.toJson
      case StringValue(s) => s.toJson
      case IntValue(n)    => n.toJson
    }
  }

  implicit val valueDecoder = new JsonDecoder[Value] {
    def jsonTo(a: String): Either[Throwable, Value] =
      a.jsonTo[String].map(Value.apply) orElse a.jsonTo[Double].map(Value.apply) orElse a.jsonTo[Int].map(Value.apply)
  }

}
