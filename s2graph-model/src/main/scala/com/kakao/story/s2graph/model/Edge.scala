package com.kakao.story
package s2graph
package model

import serialize.{JsonDecoder, JsonEncoder}
import serialize.syntax.all._
import serialize.instances.all._
import cats.syntax.either._

trait Edge

case class SingleEdge(
    timestamp: Option[Long],
    operation: Option[Operation],
    score: Option[Value],
    from: Option[Identity],
    to: Option[Identity],
    label: Option[Identity],
    direction: Option[Direction],
    props: Map[Identity, Value]
) extends Edge

case class AggEdges(
    groupBy: Option[Map[Identity, Value]],
    scoreSum: Option[Double],
    agg: Option[List[SingleEdge]]
) extends Edge

object Edge extends EdgeInstances {
  def singleEdge(
      timestamp: Option[Long],
      operation: Option[Operation],
      score: Option[Value],
      from: Option[Identity],
      to: Option[Identity],
      label: Option[Identity],
      direction: Option[Direction],
      props: Map[Identity, Value]
  ): Edge = SingleEdge(timestamp, operation, score, from, to, label, direction, props)

  def aggEdges(
      groupBy: Option[Map[Identity, Value]],
      scoreSum: Option[Double],
      agg: Option[List[SingleEdge]]
  ): Edge = AggEdges(groupBy, scoreSum, agg)
}

trait EdgeInstances {
  implicit val edgeEncoder = new JsonEncoder[Edge] { self =>
    def toJson(a: Edge): String = {
      implicit val g: JsonEncoder[Edge] = self
      a match {
        case v: SingleEdge => (v: SingleEdge).toJson
        case v: AggEdges => (v: AggEdges).toJson
      }
    }
  }

  implicit def edgeDecoder = new JsonDecoder[Edge] {
    def jsonTo(s: String): Either[Throwable, Edge] = s.jsonTo[SingleEdge] orElse s.jsonTo[AggEdges]
  }

}
