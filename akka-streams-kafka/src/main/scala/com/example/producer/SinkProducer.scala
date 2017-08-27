package com.example.producer

import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import com.example._

object SinkProducer extends App {

  val done = Source(50 to 60)
    .map(_.toString)
    .map { elem =>
      println(s"PlainSinkProducer produce: $elem")
      new ProducerRecord[Array[Byte], String](topic, elem)
    }
    .runWith(Producer.plainSink(producerSettings))
}
