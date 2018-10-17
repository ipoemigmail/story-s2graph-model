package com.kakao.story.serialize

trait JsonDecoder[A] {
  def jsonTo(s: String): Either[Throwable, A]
}

object JsonDecoder {
  def apply[A](implicit instance: JsonDecoder[A]): JsonDecoder[A] = instance

  trait ToJsonDecoderOps {
    implicit class ToJsonDecoderOps(target: String) {
      def jsonTo[A](implicit tc: JsonDecoder[A]) = tc.jsonTo(target)
    }
  }
}
