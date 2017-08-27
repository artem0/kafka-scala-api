package com.example.kafka08

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import com.example._

object KafkaStreamingWC {

  def main(args: Array[String]): Unit = {
    kafkaStreamingWC
  }

  def kafkaStreamingWC = launch(kafka08StreamingWC, "Kafka08Streaming", "checkpointing")

  /**
    * kafka08Streaming with createDirectStream
    * @param ssc StreamingContext
    */
  def kafka08StreamingWC(ssc: StreamingContext) = {
    val brokers = "127.0.0.1:9092"
    val topics = "sample_topic"
    val topicsSet = topics.split(",").toSet

    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    val lines = messages.map { case (_, value) => value }
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()
  }
}