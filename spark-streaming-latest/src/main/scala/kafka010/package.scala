import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}

package object kafka010 {

  def setupLogging(): Unit = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)
  }

  def kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "127.0.0.1:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "mygroup1",
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (true: java.lang.Boolean)
  )

  def launch(logic: StreamingContext => Unit, appName:String, checkpointPath:String): Unit = {
    val streamingContext = new StreamingContext("local[*]", appName, Seconds(2))
    setupLogging()
    logic.apply(streamingContext)

    streamingContext.checkpoint(checkpointPath)
    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
