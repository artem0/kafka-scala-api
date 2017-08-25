package kafka010

import java.{util => ju}

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.{SparkContext, TaskContext}

import scala.collection.JavaConversions._

object KafkaStreamingLatestExample {

  def main(args: Array[String]): Unit = {
    kafkaStream010()
  }

  /**
    * Kafka 0.10.0 API
    */
  def kafkaStream010() =
    launch(kafkaStreaming010, appName = "Kafka010_DirectStream", checkpointPath = "checkpointing")

  private def kafkaStreaming010(streamingContext: StreamingContext): Unit = {
    val topics = Array("sample_topic")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent, //It will consistently distribute partitions across all executors.
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value)).print()

    stream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd.foreachPartition { _ =>
        val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
      }
    }
  }

  private def kafkaRdd010() = {
    val sparkContext = new SparkContext("local[*]", "kafkaRdd010")

    val offsetRanges = Array(
      // topic, partition, inclusive starting offset, exclusive ending offset
      OffsetRange("sample_topic", 0, 10, 20),
      OffsetRange("sample_topic", 1, 10, 20)
    )
    val params = new ju.HashMap[String, Object](kafkaParams)
    val kafkaRDD =  KafkaUtils.createRDD[String, String](sparkContext, params , offsetRanges, PreferConsistent)
    println(kafkaRDD.map(_.value()).first())
  }

}