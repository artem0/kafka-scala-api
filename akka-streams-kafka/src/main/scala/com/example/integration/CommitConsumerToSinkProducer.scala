package com.example.integration
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ProducerMessage, Subscriptions}
import org.apache.kafka.clients.producer.ProducerRecord

import com.example._

object CommitConsumerToSinkProducer {

  /**
    * Consume from from one topic and write to another with Sinks
    */
  val done = Consumer.committableSource(consumerSettings, Subscriptions.topics(topic1))
    .map { msg =>
      println(s"topic1 -> topic2: $msg")
      ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
        topic2,
        msg.record.value
      ), msg.committableOffset)
    }
    .runWith(Producer.commitableSink(producerSettings))

}
