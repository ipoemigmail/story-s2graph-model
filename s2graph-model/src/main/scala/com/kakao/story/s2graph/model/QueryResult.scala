package com.kakao.story
package s2graph
package model

case class QueryResult(size: Long, degrees: List[Degree], results: List[Edge])
