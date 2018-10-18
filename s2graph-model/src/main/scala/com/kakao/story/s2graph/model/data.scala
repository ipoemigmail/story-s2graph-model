package com.kakao.story
package s2graph

import cats.data.NonEmptyList

package object model {
  type NEL[+A] = NonEmptyList[A]

  case object NEL {
    def unapply[A](a: NEL[A]) = NonEmptyList.unapply(a)
    def apply[A](a: A, as: A*): NEL[A] = NonEmptyList(a, as.toList)
  }

}
