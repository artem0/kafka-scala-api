package com

import cakesolutions.kafka.KafkaProducer
import cakesolutions.kafka.KafkaProducer.Conf
import org.apache.kafka.common.serialization.StringSerializer

package object example {

  val topic = "sample_topic"

  val kafkaProducer = KafkaProducer(
    Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092")
  )
}
