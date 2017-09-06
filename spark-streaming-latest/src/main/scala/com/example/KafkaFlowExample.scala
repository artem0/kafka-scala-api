package com.example

import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.dstream.DStream._
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.util.Try

case class Purchase(item_id: String, amount: BigDecimal, time: Long)
case class Key(item_id: String, time: DateTime)
case class Summary(item_id: String, time: DateTime, total: BigDecimal)

object KafkaFlowExample {
  implicit val formats = DefaultFormats

  def extract(message: String): Option[(Key, BigDecimal)] = {
    for {
      parsed <- Try(parse(message)).toOption
      purchase <- parsed.extractOpt[Purchase]
    } yield {
      val datetime = new DateTime(purchase.time)
      val roundedTime = datetime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
      Key(purchase.item_id, roundedTime) -> purchase.amount
    }
  }

  def transformStream(stream: InputDStream[String]): DStream[Summary] = {
    stream
      .flatMap(extract)
      .reduceByKey(_ + _)
      .map { case (key, amount) =>
        Summary(key.item_id, key.time, amount)
      }
  }
}