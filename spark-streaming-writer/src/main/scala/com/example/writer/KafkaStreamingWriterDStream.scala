package com.example.writer

import com.github.benfradet.spark.kafka010.writer._
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


object KafkaStreamingWriterDStream extends App {

  //Streaming from socket - nc -lk 9087
  val ssc = new StreamingContext("local[*]", "DStream", Seconds(2))
  setupLogging()

  val lines: ReceiverInputDStream[String] = ssc.socketTextStream("localhost", 9087)

  lines.writeToKafka(numbersProducerConfig,
    s => new ProducerRecord[String, String](topic, "key " + s , s.toString)
  )

  ssc.start()
  ssc.awaitTermination()
}
