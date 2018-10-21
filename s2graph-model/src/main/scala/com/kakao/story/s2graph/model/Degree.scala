package com.kakao.story
package s2graph
package model

final case class Degree(
    from: Value,
    label: Identity,
    direction: Direction,
    _degree: Int
)
