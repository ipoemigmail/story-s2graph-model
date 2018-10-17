package com.kakao.story
package s2graph
package model

case class Edge(
    timestamp: Option[Long],
    operation: Option[Operation],
    score: Option[Long],
    from: Option[Identity],
    to: Option[Identity],
    label: Option[Identity],
    direction: Option[Direction],
    props: Map[Identity, Value]
)
