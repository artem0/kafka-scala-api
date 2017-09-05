package com.example.structured_streaming

import org.apache.spark.sql.{Dataset, SparkSession}

object StructuredStreamingKafkaSample extends App {

  val sparkSession = SparkSession
    .builder
    .master("local")
    .appName("kafka")
    .getOrCreate()

  sparkSession.sparkContext.setLogLevel("ERROR")

  import sparkSession.implicits._

  val kafkaDF = sparkSession
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "127.0.0.1:9092")
    .option("subscribe", "structured_topic")
    .load()

  val data: Dataset[(String, String)] = kafkaDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
    .as[(String, String)]
  kafkaDF.printSchema()

  data.writeStream
    .outputMode("append")
    .format("console")
    .start()
    .awaitTermination()

}
