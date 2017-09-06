package com.example.avro

case class KafkaPayload(key: Option[Array[Byte]], value: Array[Byte])