/**
  * Created by arukavyt on 25.08.17.
  */
package object samples {

  def setupLogging(): Unit = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)
  }
}
