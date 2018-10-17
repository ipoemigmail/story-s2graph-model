package com.kakao.story
package s2graph
package model

case class Vertex(
    timestamp: Option[Long] = None,
    serviceName: Identity,
    columnName: Identity,
    id: Option[Value],
    ids: Option[NEL[Value]] = None,
    props: Option[Map[Identity, Value]] = None
)
