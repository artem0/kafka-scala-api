package samples

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

object KafkaStreamingGroups {

  private def kafkaStreamingGroup = {
    val zkQuorum = "127.0.0.1:2181"
    val group = "mygroup1"
    val topics = "sample_topic"
    val numThreads = "1"

    val ssc = new StreamingContext("local[*]", "DirectKafkaWordCount", Seconds(2))
    setupLogging()

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map { case (_, value) => value }
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(2), 2)
    wordCounts.print()

    ssc.checkpoint("checkpoint")
    ssc.start()
    ssc.awaitTermination()
  }
}
