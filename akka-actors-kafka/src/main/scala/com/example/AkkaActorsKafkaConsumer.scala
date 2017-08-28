package com.example

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, Extractor, KafkaConsumerActor}

import scala.concurrent.duration._

object AkkaActorsKafkaConsumer extends App {
  ConsumerRecovery()
}

object ConsumerRecovery {
  def apply(): ActorRef ={
    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)
    val system = ActorSystem()
    system.actorOf(Props(new ConsumerRecovery(kafkaConsumerConf, actorConf)))
  }
}

class ConsumerRecovery(kafkaConfig: KafkaConsumer.Conf[String, String],
                        actorConfig: KafkaConsumerActor.Conf) extends Actor with ActorLogging {

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10) {
    case _: KafkaConsumerActor.ConsumerException =>
      log.info("Consumer exception caught. Restarting consumer.")
      SupervisorStrategy.Restart
    case _ =>
      SupervisorStrategy.Escalate
  }

  val recordsExt: Extractor[Any, ConsumerRecords[String, String]] = ConsumerRecords.extractor[String, String]

  val consumer: ActorRef = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )

  consumer ! Subscribe.AutoPartition(List(topic))

  override def receive: Receive = {
    // Consume from Kafka
    case recordsExt(records) =>
      processRecords(records.pairs)
      sender() ! Confirm(records.offsets, commit = true)
  }

  private def processRecords(records: Seq[(Option[String], String)]) =
    records.foreach { case (key, value) =>
      log.info(s"Received [$key,$value]")
    }
}