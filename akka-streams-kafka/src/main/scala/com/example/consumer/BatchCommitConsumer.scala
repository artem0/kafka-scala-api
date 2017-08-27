package com.example.consumer

import akka.Done
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import com.example._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BatchCommitConsumer extends App {

  val done = Consumer.committableSource(consumerSettings, Subscriptions.topics(topic))
    .mapAsync(1) { msg =>
      println(s"BatchCommittableConsumer consume: $msg")
      Future.successful(Done).map(_ => msg.committableOffset)
    }
    .batch(max = 20, first => CommittableOffsetBatch.empty.updated(first)) { (batch, elem) =>
      batch.updated(elem)
    }
    .mapAsync(3)(_.commitScaladsl())
    .runWith(Sink.ignore)
}
