package com.kakao.story.serialize
import io.circe.Encoder
import io.circe.Printer
import simulacrum.typeclass

@typeclass
trait JsonEncoder[A] {
  def toJson(a: A): String
}
