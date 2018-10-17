package com.kakao.story
package s2graph
package model

case class StepDetail(
    label: Identity,
    direction: Option[Direction] = None,
    limit: Option[Int] = Some(10),
    offset: Option[Int] = Some(0),
    index: Option[Identity] = None,
    interval: Option[Interval] = None,
    duration: Option[Duration] = None,
    scoring: Option[Map[Identity, Double]] = None,
    where: Option[String] = None,
    outputField: Option[Identity] = None,
    exclude: Option[Boolean] = Some(false),
    include: Option[Boolean] = Some(false),
    rpcTimeout: Option[Int] = Some(100),
    maxAttempt: Option[Int] = Some(1),
    _to: Option[Identity] = None,
    threshold: Option[Double] = Some(0.0),
    transform: Option[NEL[NEL[Identity]]] = None,
    cacheTTL: Option[Long] = None
)
