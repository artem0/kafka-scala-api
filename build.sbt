import Dependencies.versions._

val sparkStreamingOld = "spark-streaming-old"
val sparkStreamingLatest = "spark-streaming-latest"
val akkaStreamsKafka = "akka-streams-kafka"
val akkaActorsKafka = "akka-actors-kafka"
val structuredStreamingModule = "structured-streaming"
val streamingWriter = "spark-streaming-writer"

val sparkCommon = Seq("org.apache.spark" %% "spark-core" % sparkVersion,
                      "org.apache.spark" %% "spark-streaming" % sparkVersion)


lazy val sparkKafka08 = (project in file(sparkStreamingOld))
  .settings(commonSettings(sparkStreamingOld): _*)
  .settings(
    libraryDependencies ++= sparkCommon ++ Seq(
      "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % sparkVersion
    )
  )

lazy val sparkKafka010 = (project in file(sparkStreamingLatest))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= sparkCommon ++ Seq(
      "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVersion,
      "com.twitter" %% "bijection-avro" % "0.8.1",
      "joda-time" % "joda-time" % "2.8.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "org.specs2" % "specs2-core_2.11" % "3.9.5" % "test"
    )
  )

lazy val structuredStreaming = (project in file(structuredStreamingModule))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % sparkVersion,
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion
    )
  )

lazy val sparkStreamingWriter = (project in file(streamingWriter))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka_2.11" % "0.10.2.1",
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "com.github.benfradet" %% "spark-kafka-0-10-writer" % "0.3.0",
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion
    )
  )

lazy val akkaKafkaModule = (project in file(akkaStreamsKafka))
  .settings(commonSettings(akkaStreamsKafka): _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamsKafkaVersion
    )
  )

lazy val akkaActorsKafkaModule = (project in file(akkaActorsKafka))
  .settings(commonSettings(akkaActorsKafka): _*)
  .settings(
    libraryDependencies ++= Seq(
      "net.cakesolutions" %% "scala-kafka-client" % scalaKafkaClient,
      "net.cakesolutions" %% "scala-kafka-client-akka" % scalaKafkaClient
    )
  )

def commonSettings(_name: String) = Seq(
  name := _name,
  scalaVersion := "2.11.8",
  version := "1.0",
  parallelExecution in Test := false,
  cancelable in Global := true,
  resolvers ++= List(
    Resolver.jcenterRepo,
    Resolver.sonatypeRepo("releases"), Resolver.bintrayRepo("cakesolutions", "maven"),
    "apache-snapshots" at "http://repository.apache.org/snapshots/"
  )
)

