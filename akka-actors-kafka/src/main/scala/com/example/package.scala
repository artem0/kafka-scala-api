package com

import java.util.Collections

import cakesolutions.kafka.{KafkaConsumer, KafkaProducer}
import cakesolutions.kafka.KafkaProducer.Conf
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

package object example {

  val topic = "sample_topic"

  val kafkaProducer = KafkaProducer(
    Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092")
  )

  val kafkaProducerConf = KafkaProducer.Conf(
    new StringSerializer, new StringSerializer,
    bootstrapServers = "localhost:9092"
  )

  val kafkaConsumerConf = KafkaConsumer.Conf(
    new StringDeserializer,
    new StringDeserializer,
    groupId = "test_group",
    enableAutoCommit = false,
    autoOffsetReset = OffsetResetStrategy.EARLIEST,
    bootstrapServers = "localhost:9092")
}
