package com.kakao.story
package s2graph
package model

import serialize.JsonEncoder
import serialize.syntax.all._

sealed trait Duplicate

object Duplicate extends DuplicateInstances {

  val First: Duplicate = first
  val Sum: Duplicate = sum
  val CountSum: Duplicate = countSum
  val Raw: Duplicate = raw

  private[Duplicate] object first extends Duplicate

  private[Duplicate] object sum extends Duplicate

  private[Duplicate] object countSum extends Duplicate

  private[Duplicate] object raw extends Duplicate

}

trait DuplicateInstances {
  implicit val duplicateEncoder = new JsonEncoder[Duplicate] {
    def toJson(a: Duplicate): String =
      (a match {
        case Duplicate.Sum      => "sum"
        case Duplicate.CountSum => "countSum"
        case Duplicate.Raw      => "raw"
        case _                  => "first"
      }).toJson
  }
}
