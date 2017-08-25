package samples

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaStreamingWC {

  /**
    * Kafka 0.8 API
    */
  def kafkaStreamingWC() = {
    val ssc = new StreamingContext("local[*]", "DirectKafkaStream", Seconds(2))
    setupLogging()
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

    // Start the computation
    ssc.checkpoint("checkpointing")
    ssc.start()
    ssc.awaitTermination()
  }
}