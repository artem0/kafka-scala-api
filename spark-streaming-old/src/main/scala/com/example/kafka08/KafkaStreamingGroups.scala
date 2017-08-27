package com.example.kafka08

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

import com.example._

object KafkaStreamingGroups {

  def main(args: Array[String]): Unit = {
    kafkaStreamingGroup
  }

  def kafkaStreamingGroup = launch(kafka08Streaming, "Kafka08Streaming", "checkpointing")

  /**
    * kafka08Streaming with createStream
    * @param ssc StreamingContext
    */
  private def kafka08Streaming(ssc: StreamingContext) = {
    val zkQuorum = "127.0.0.1:2181"
    val group = "mygroup1"
    val topics = "sample_topic"
    val numThreads = "1"

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map { case (_, value) => value }
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(2), 2)
    wordCounts.print()
  }
}
