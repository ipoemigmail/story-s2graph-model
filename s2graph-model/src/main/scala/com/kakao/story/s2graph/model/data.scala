package com.kakao.story
package s2graph

import cats.data.NonEmptyList

package object model {
  type NEL[+A] = NonEmptyList[A]

  object NEL {
    def apply[A](a: A, as: A*): NEL[A] = NonEmptyList(a, as.toList)
  }

}
