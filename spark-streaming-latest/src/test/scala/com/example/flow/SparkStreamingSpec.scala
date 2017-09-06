package com.example.flow

import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream._
import org.specs2.mutable._

import scala.collection.mutable

class SparkStreamingSpec[T](val seq: Seq[String])(implicit val fun: InputDStream[String] => DStream[T]) extends After {
  lazy val ssc = new StreamingContext("local", "test", Seconds(1))
  val rdd = ssc.sparkContext.makeRDD(seq)

  val stream = new ConstantInputDStream(ssc, rdd)

  val collector = mutable.MutableList[T]()

  fun(stream).foreachRDD(rdd => collector ++= rdd.collect())

  ssc.start()
  ssc.awaitTerminationOrTimeout(1000)

  def after = ssc.stop()
}