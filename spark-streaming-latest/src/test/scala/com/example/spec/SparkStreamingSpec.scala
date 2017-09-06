package com.example.spec

import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}
import org.scalatest.Suite

trait SparkStreamingSpec extends SparkSpec {
  this: Suite =>

  import java.nio.file.Files

  private var streamingContext: StreamingContext = _

  override def beforeAll(): Unit = {
    super.beforeAll()

    streamingContext = new StreamingContext(sc, batchDuration)
    streamingContext.checkpoint(checkpointDir)
  }

  def batchDuration: Duration = Seconds(1)

  def checkpointDir: String = Files.createTempDirectory(this.getClass.getSimpleName).toUri.toString

  override def afterAll(): Unit = {
    if (streamingContext != null) {
      streamingContext.stop(stopSparkContext = false, stopGracefully = false)
      streamingContext = null
    }

    super.afterAll()
  }

  override def sparkConfig: Map[String, String] = {
    super.sparkConfig + ("spark.streaming.clock" -> "org.apache.spark.streaming.util.ManualClock")
  }

  def ssc: StreamingContext = streamingContext

}