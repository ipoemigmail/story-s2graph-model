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
import com.sun.jdi.LongValue

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

  it should "" in {
    import s2graph.model._
    import java.time.{ZonedDateTime => ZDT}
    import inhouse.utils.StoryId

    val now = ZDT.now.toInstant.toEpochMilli

    val daisy = Value("3712378").some
    val me = Value("25732910").some

    val srcVertices = NEL(
      Vertex(
        serviceName = Identity("kakaostory"),
        columnName = Identity("profile_id"),
        id = me
      )
    )

    def recentReadContents(n: Int) = StepDetail(
      label = "kakaostory_3tab_user_doc_action",
      offset = 0.some,
      limit = n.some,
      direction = Direction.Out.some,
      index = Identity("_IDX_ACTION").some,
      duration = Duration(0, now).some,
      interval = Interval(Map(Identity("action") -> Value("click")), Map(Identity("action") -> Value("click"))).some
    )

    val contentsReaders = StepDetail(
      label = "kakaostory_3tab_user_doc_action",
      offset = 0.some,
      limit = 10.some,
      direction = Direction.In.some,
      index = Identity("_IDX_ACTION").some,
      scoring = Map(Identity("score") -> 1.0).some,
      duration = Duration(0, now).some,
      interval = Interval(Map(Identity("action") -> Value("click")), Map(Identity("action") -> Value("click"))).some
    )

    val graphQuery1 = GraphQuery.single(
      groupBy = NEL(Identity("to"), Identity("image_path")).some,
      srcVertices = srcVertices,
      steps = NEL(
        Step(step = NEL(recentReadContents(5))),
        Step(step = NEL(contentsReaders)),
        Step(step = NEL(recentReadContents(10)))
      )
    )

    val multiQuery = GraphQuery.multi(
      queries = NEL(
        graphQuery1,
        graphQuery1,
      ))

    println(graphQuery1.toJson)

    val result = requests.post(url = "http://graph-query.iwilab.com:9000/graphs/getEdges",
                               headers = List("Content-Type" -> "application/json"),
                               data = graphQuery1.toJson)

    import inhouse.utils.StoryId

    def parseIdentity(a: Identity) = a match {
      case LongIdentity(v)   => v.toString
      case StringIdentity(v) => v.toString
    }

    def parseValue(a: Value) = a match {
      case StringValue(v) => v.toString
      case DoubleValue(v) => v.toString
      case IntValue(v) => v.toString
    }

    decode[QueryResult](result.text) match {
      case Left(t)  => println(t)
      case Right(queryResult) => queryResult.results.map(edge => edge match {
        case SingleEdge(_, _, _, _, to, _, _, _) => StoryId.activityPermalink("real", parseIdentity(to.get))
        case AggEdges(Some(group), _, Some(agg)) => StoryId.activityPermalink("real", parseValue(group(Identity("to"))))
      }).foreach(println)
    }
  }

}
