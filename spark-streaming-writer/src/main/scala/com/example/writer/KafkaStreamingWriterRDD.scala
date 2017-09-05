package com.example.writer

import com.github.benfradet.spark.kafka010.writer._
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.sql.SparkSession

object KafkaStreamingWriterRDD extends App {

  val spark = SparkSession
    .builder
    .master("local")
    .appName("streaming-writer")
    .getOrCreate()

  setupLogging()

  val numbersRDD = spark.range(400, 420).rdd

  numbersRDD.writeToKafka(
    numbersProducerConfig,
    s => new ProducerRecord[String, String](topic, "key " + s , s.toString)
  )
}
