package samples

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaStreamingLatestExample {

  def main(args: Array[String]): Unit = {
    kafkaStream10()
  }

  /**
    * Kafka 1.0 API
    */
  def kafkaStream10() = {
    val streamingContext = new StreamingContext("local[*]", "DirectKafkaStream", Seconds(2))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "127.0.0.1:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "mygroup1",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )

    val topics = Array("first_topic")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value))

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}