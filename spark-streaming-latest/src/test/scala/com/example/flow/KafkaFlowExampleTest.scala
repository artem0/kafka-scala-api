package com.example.flow

import org.apache.log4j.{Level, Logger}
import org.specs2.mutable._

import scala.Predef.{conforms => _}

class KafkaFlowExampleTest extends Specification {
  Logger.getRootLogger.setLevel(Level.ERROR)

  sequential

  "the transformStream method" should {
    implicit val fun = KafkaFlowExample.transformStream _

    "with 10 identical records" should {
      val records = Seq.fill(10)("""{"item_id":"abc123","amount":1.23,"time":1431504603105}""")
      "return a single record with the correct total" in new SparkStreamingSpec(records) {
        collector.length mustEqual 1
        val output = collector.head
        output.total mustEqual BigDecimal(12.3)
      }
    }

    "with invalid records" should {
      val records = Seq("this is not json", """{"this":"isn't in the right format"}""")
      "output no records" in new SparkStreamingSpec(records) {
        collector.length mustEqual 0
      }
    }
  }
}