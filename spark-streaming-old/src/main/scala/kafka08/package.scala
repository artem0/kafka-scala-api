import org.apache.spark.streaming.{Seconds, StreamingContext}

package object kafka08 {

    def setupLogging(): Unit = {
      import org.apache.log4j.{Level, Logger}
      val rootLogger = Logger.getRootLogger
      rootLogger.setLevel(Level.ERROR)
    }

    def launch(logic: StreamingContext => Unit, appName:String, checkpointPath:String): Unit = {
      val streamingContext = new StreamingContext("local[*]", appName, Seconds(2))
      setupLogging()
      logic.apply(streamingContext)

      streamingContext.checkpoint(checkpointPath)
      streamingContext.start()
      streamingContext.awaitTermination()
    }
}
