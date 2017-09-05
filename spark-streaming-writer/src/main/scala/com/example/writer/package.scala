package com.example

import java.util.Properties

import org.apache.kafka.common.serialization.StringSerializer

package object writer {

  val topic = "first_topic"
  val numbersProducerConfig = Map(
    "bootstrap.servers" -> "127.0.0.1:9092",
    "key.serializer" -> classOf[StringSerializer].getName,
    "value.serializer" -> classOf[StringSerializer].getName
  )

  implicit def buildPropertiesFromMap(properties: Map[String, String]): Properties =
    (new Properties /: properties) {
      case (a, (k, v)) =>
        a.put(k,v)
        a
    }

  def setupLogging(): Unit = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)
  }

}
