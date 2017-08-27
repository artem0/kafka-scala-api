package com.example.consumer

import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import com.example._

object CommittableSourceConsumer extends App {

  val done =
    Consumer.committableSource(consumerSettings, Subscriptions.topics(topic))
      .mapAsync(1) { msg =>
        println(s"Consume message: $msg")
        msg.committableOffset.commitScaladsl()
      }
      .runWith(Sink.ignore)
}
