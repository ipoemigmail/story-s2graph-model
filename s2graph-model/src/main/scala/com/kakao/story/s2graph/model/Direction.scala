package com.kakao.story
package s2graph
package model

import cats.syntax.either._
import serialize.{JsonDecoder, JsonEncoder}
import serialize.syntax.all._

sealed trait Direction

object Direction extends DirectionInstances {

  val Out: Direction = out
  val In: Direction = in
  val Undirected: Direction = undirected

  private[Direction] object out extends Direction

  private[Direction] object in extends Direction

  private[Direction] object undirected extends Direction

}

trait DirectionInstances {
  implicit val directionEncoder = new JsonEncoder[Direction] {
    def toJson(a: Direction): String =
      (a match {
        case Direction.Out => "out"
        case Direction.In  => "in"
        case _             => "undirected"
      }).toJson
  }

  implicit val directionDecoder = new JsonDecoder[Direction] {
    def jsonTo(a: String): Either[Throwable, Direction] = a.jsonTo[String].map {
      case "out" => Direction.Out
      case "in"  => Direction.In
      case _     => Direction.Undirected
    }
  }

}
