package com.example

import cakesolutions.kafka.KafkaProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

import scala.collection.immutable.Seq
import scala.concurrent.{ExecutionContext, Future}

object AkkaKafkaProducer extends App {
  implicit val ec = ExecutionContext.global

  val transformer: (Int => Future[RecordMetadata]) =
    (num: Int) => kafkaProducer.send(KafkaProducerRecord(topic, Some("key"), "text - " + num))

  val callback = 0 to 5 map transformer

  import scala.util.{Failure, Success}
  private def lift[T](futures: Seq[Future[T]]) =
    futures.map(_.map { Success(_) }.recover { case t => Failure(t) })

  def waitAll[T](futures: Seq[Future[T]]) =
    Future.sequence(lift(futures))

  waitAll(callback).map(p => println( "size " + p.seq.size))
}
