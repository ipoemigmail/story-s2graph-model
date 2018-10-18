package com.kakao.story
package serialize
package instances

import cats.data.NonEmptyList
import org.scalatest._
import io.circe.parser.decode
import syntax.all._
import instances.circe._
import io.circe.Json
import cats.syntax.all._

class CirceInstancesTest extends FlatSpec with Matchers {

  "JsonDecoder" should "decode Standard Primary types" in {
    "1".jsonTo[Int] should be(1.asRight)

    "1.0".jsonTo[Double] should be(1.0.asRight)

    "1".jsonTo[Long] should be(1L.asRight)

    "\"s\"".jsonTo[String] should be("s".asRight)

    "[1, 2, 3]".jsonTo[List[Int]] should be(List(1, 2, 3).asRight)

    "[1, 2, 3]".jsonTo[NonEmptyList[Int]] should be(NonEmptyList(1, List(2, 3)).asRight)

    "{\"field1\": 1, \"field2\": 2}".jsonTo[Map[String, Int]] should be(Map("field1" -> 1, "field2" -> 2).asRight)
  }

  it should "decode User Defined types (No JsonEncoder)" in {
    import s2graph.model._

    //"{ \"from\": 1, \"to\": 10}".jsonTo[Duration] should be(Duration(1, 10).asRight)
  }

  it should "decode User Defined types (JsonEncoder)" in {
    import s2graph.model._

    "\"a\"".jsonTo[Identity] should be(Identity("a").asRight)

    "\"a\"".jsonTo[Value] should be(Value("a").asRight)
    "1.0".jsonTo[Value] should be(Value(1.0).asRight)
  }

  it should "decode Complex type" in {
    import s2graph.model._

    "{\"a\": 1}".jsonTo[Map[Identity, Value]] should be(Map(Identity("a") -> Value(1.0)).asRight)

    val graphQuery = GraphQuery.single(
      srcVertices = NEL(
        Vertex(
          serviceName = "kakaostory",
          columnName = "doc_id",
          id = Value("89532167_100716066432862003").some
        )
      ),
      steps = NEL(
        Step(
          step = NEL(
            StepDetail(
              label = Identity("kakaostory_3tab_user_doc_action"),
              direction = Direction.In.some,
              limit = 5.some,
              offset = 0.some,
              duration = Duration(0, 1539581619424L).some
            )
          )
        )
      )
    )

    """{
      |  "srcVertices" : [
      |    {
      |      "serviceName" : "kakaostory",
      |      "columnName" : "doc_id",
      |      "id" : "89532167_100716066432862003"
      |    }
      |  ],
      |  "steps" : [
      |    {
      |      "step" : [
      |        {
      |          "label" : "kakaostory_3tab_user_doc_action",
      |          "direction" : "in",
      |          "limit" : 5,
      |          "offset" : 0,
      |          "duration" : {
      |            "from" : 0,
      |            "to" : 1539581619424
      |          },
      |          "exclude" : false,
      |          "include" : false,
      |          "rpcTimeout" : 100,
      |          "maxAttempt" : 1,
      |          "threshold" : 0.0
      |        }
      |      ]
      |    }
      |  ],
      |  "removeCycle" : true,
      |  "filterOutFields" : [
      |    "_to"
      |  ]
      |}""".stripMargin.jsonTo[GraphQuery] should be(graphQuery.asRight)
  }

  @inline def formatJson(text: String) = text.jsonTo[Json].right.get.toJson

  "JsonEncoder" should "encode Standard Primary types" in {
    1.toJson should be("1")

    "1".toJson should be("\"1\"")

    2.0.toJson should be("2.0")

    (1).some.toJson should be("1")

    None.toJson should be("null")

    Map("1" -> 1).toJson should be(formatJson("{\"1\": 1}"))

    List(1, 1, 1).toJson should be(formatJson("[1, 1, 1]"))

    val m = Map("1" -> 1)
    List(m, m, m).toJson should be(formatJson("[{\"1\": 1}, {\"1\": 1}, {\"1\": 1}]"))
  }

  it should "encode User Defined types (No JsonEncoder) " in {
    import s2graph.model.Duration

    //Duration(1, 10).toJson should be(formatJson(("{\"from\":1, \"to\":10}")))
  }

  it should "encode User Defined types (with JsonEncoder) " in {
    import s2graph.model.Identity

    Identity("1").toJson should be("\"1\"")
  }

  it should "encode Standard Primary type with User Defined types " in {
    import s2graph.model.Identity
    import s2graph.model.Value

    Map(Identity("1") -> Value(1)).toJson should be(formatJson("{\"1\": 1}"))
  }

  it should "encode Complex type" in {
    import s2graph.model._

    val graphQuery = GraphQuery.single(
      srcVertices = NEL(
        Vertex(
          serviceName = "kakaostory",
          columnName = "doc_id",
          id = Value("89532167_100716066432862003").some
        )),
      steps = NEL(
        Step(
          step = NEL(
            StepDetail(
              label = Identity("kakaostory_3tab_user_doc_action"),
              direction = Direction.In.some,
              limit = 5.some,
              offset = 0.some,
              duration = Duration(0, 1539581619424L).some
            )))
      )
    )
    val result =
      """{
        |  "srcVertices" : [
        |    {
        |      "serviceName" : "kakaostory",
        |      "columnName" : "doc_id",
        |      "id" : "89532167_100716066432862003"
        |    }
        |  ],
        |  "steps" : [
        |    {
        |      "step" : [
        |        {
        |          "label" : "kakaostory_3tab_user_doc_action",
        |          "direction" : "in",
        |          "limit" : 5,
        |          "offset" : 0,
        |          "duration" : {
        |            "from" : 0,
        |            "to" : 1539581619424
        |          },
        |          "exclude" : false,
        |          "include" : false,
        |          "rpcTimeout" : 100,
        |          "maxAttempt" : 1,
        |          "threshold" : 0.0
        |        }
        |      ]
        |    }
        |  ],
        |  "removeCycle" : true,
        |  "filterOutFields" : [
        |    "_to"
        |  ]
        |}""".stripMargin
    graphQuery.toJson should be(formatJson(result))
  }

}
