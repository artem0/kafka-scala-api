package com.example.integration

import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ProducerMessage, Subscriptions}
import akka.stream.scaladsl.Sink
import com.example._
import org.apache.kafka.clients.producer.ProducerRecord

object CommitConsumerToFlowProducer extends App {

  /**
    * Consume from from one topic and write to another with Flows
    */
  val done = Consumer.committableSource(consumerSettings, Subscriptions.topics(topic1))
    .map { msg =>
      println(s"topic1 -> topic2: $msg")
      ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
        topic2,
        msg.record.value
      ), msg.committableOffset)
    }
    .via(Producer.flow(producerSettings))
    .mapAsync(producerSettings.parallelism) { result =>
      result.message.passThrough.commitScaladsl()
    }
    .runWith(Sink.ignore)
}
