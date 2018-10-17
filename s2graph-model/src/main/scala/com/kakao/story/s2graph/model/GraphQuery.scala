package com.kakao.story
package s2graph
package model

case class GraphQuery(
    srcVertices: NEL[Vertex],
    steps: NEL[Step],
    removeCycle: Option[Boolean] = Some(true),
    select: Option[NEL[Identity]] = None,
    groupBy: Option[NEL[Identity]] = None,
    filterOut: Option[GraphQuery] = None,
    filterOutFields: Option[NEL[Identity]] = Some(NEL("_to"))
)
