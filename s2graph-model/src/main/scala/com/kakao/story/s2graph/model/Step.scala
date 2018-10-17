package com.kakao.story
package s2graph
package model

case class Step(
    step: NEL[StepDetail],
    weights: Option[Map[Identity, Double]] = None,
    nextStepThreshold: Option[Double] = None,
    nextStepLimit: Option[Double] = None,
    sample: Option[Int] = None
)
