package com.kakao.story
package s2graph
package model

import serialize.{JsonDecoder, JsonEncoder}
import serialize.instances.all._
import serialize.syntax.all._
import cats.syntax.either._

sealed abstract class GraphQuery

final case class SingleQuery(
    srcVertices: NEL[Vertex],
    steps: NEL[Step],
    removeCycle: Option[Boolean] = Some(true),
    select: Option[NEL[Identity]] = None,
    groupBy: Option[NEL[Identity]] = None,
    orderBy: Option[NEL[Map[Identity, Identity]]] = None,
    filterOut: Option[GraphQuery] = None,
    filterOutFields: Option[NEL[Identity]] = Some(NEL(Identity.unsafeApply("_to")))
) extends GraphQuery

final case class MultiQuery(
    returnDegree: Option[Boolean] = None,
    returnAgg: Option[Boolean] = None,
    groupBy: Option[NEL[Identity]] = None,
    filterOut: Option[GraphQuery] = None,
    orderBy: Option[NEL[Map[Identity, Identity]]] = None,
    queries: NEL[GraphQuery]
) extends GraphQuery

object GraphQuery extends GraphQueryInstances {
  def single(
      srcVertices: NEL[Vertex],
      steps: NEL[Step],
      removeCycle: Option[Boolean] = Some(true),
      select: Option[NEL[Identity]] = None,
      groupBy: Option[NEL[Identity]] = None,
      orderBy: Option[NEL[Map[Identity, Identity]]] = None,
      filterOut: Option[GraphQuery] = None,
      filterOutFields: Option[NEL[Identity]] = Some(NEL(Identity.unsafeApply("_to")))
  ): GraphQuery = SingleQuery(srcVertices, steps, removeCycle, select, groupBy, orderBy, filterOut, filterOutFields)

  def multi(
      returnDegree: Option[Boolean] = None,
      returnAgg: Option[Boolean] = None,
      groupBy: Option[NEL[Identity]] = None,
      filterOut: Option[GraphQuery] = None,
      orderBy: Option[NEL[Map[Identity, Identity]]] = None,
      queries: NEL[GraphQuery]
  ): GraphQuery = MultiQuery(returnDegree, returnAgg, groupBy, filterOut, orderBy, queries)
}

trait GraphQueryInstances {

  implicit val graphQueryEncoder = new JsonEncoder[GraphQuery] { self =>
    def toJson(a: GraphQuery): String = {
      implicit val g: JsonEncoder[GraphQuery] = self
      a match {
        case v: SingleQuery => (v: SingleQuery).toJson
        case v: MultiQuery  => (v: MultiQuery).toJson
      }
    }
  }

  implicit def graphQueryDecoder = new JsonDecoder[GraphQuery] {
    def jsonTo(s: String): Either[Throwable, GraphQuery] = s.jsonTo[SingleQuery] orElse s.jsonTo[MultiQuery]
  }

}
