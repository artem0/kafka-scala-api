package com.example.producer

import akka.kafka.ProducerMessage
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Sink, Source}
import org.apache.kafka.clients.producer.ProducerRecord
import com.example._

object FlowProducer extends App {

  val done = Source(100 to 111)
    .map { n =>
      val partition = 1
      ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
        topic , partition, null, n.toString
      ), n)
    }
    .via(Producer.flow(producerSettings))
    .map { result =>
      val record = result.message.record
      println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value}" +
        s"(${result.message.passThrough})")
      result
    }
    .runWith(Sink.ignore)
}
