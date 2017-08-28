package com.example

import akka.actor.ActorSystem
import cakesolutions.kafka.KafkaProducerRecord
import cakesolutions.kafka.akka.{KafkaProducerActor, ProducerRecords}
import org.apache.kafka.clients.producer.ProducerRecord

object AkkaActorKafkaProduder {
  def main(args: Array[String]): Unit = {
    actorProducer()
  }

  private def actorProducer() = {
    val system = ActorSystem()
    val producer = system.actorOf(KafkaProducerActor.props(kafkaProducerConf))
    val batch: Seq[ProducerRecord[String, String]] = Seq(
      KafkaProducerRecord(topic, "foo"),
      KafkaProducerRecord(topic, "key", "value"),
      KafkaProducerRecord(topic, "bar")
    )
    val message = ProducerRecords(batch)

    producer ! message
  }
}
