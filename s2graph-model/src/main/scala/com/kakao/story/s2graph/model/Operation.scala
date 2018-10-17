package com.kakao.story
package s2graph
package model

import serialize.{JsonDecoder, JsonEncoder}
import serialize.syntax.all._

import cats.syntax.either._

sealed trait Operation

object Operation extends OperationInstances {

  val Insert: Operation = insert
  val Delete: Operation = delete
  val Update: Operation = update
  val Increment: Operation = increment

  private[Operation] object insert extends Operation

  private[Operation] object delete extends Operation

  private[Operation] object update extends Operation

  private[Operation] object increment extends Operation

}

trait OperationInstances {
  implicit val operationEncoder = new JsonEncoder[Operation] {
    def toJson(a: Operation): String =
      (a match {
        case Operation.Delete    => "delete"
        case Operation.Update    => "update"
        case Operation.Increment => "increment"
        case _                   => "insert"
      }).toJson
  }

  implicit val operationDecoder = new JsonDecoder[Operation] {
    def jsonTo(a: String): Either[Throwable, Operation] = a.jsonTo[String].map {
      case "insert"    => Operation.Insert
      case "delete"    => Operation.Delete
      case "update"    => Operation.Update
      case "increment" => Operation.Increment

      case "d"  => Operation.Delete
      case "u"  => Operation.Update
      case "in" => Operation.Increment
      case _    => Operation.Insert
    }
  }

}
