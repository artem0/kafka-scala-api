import Dependencies.versions._

val sparkStreamingOld = "spark-streaming-old"
val sparkStreamingLatest = "spark-streaming-latest"
val akkaStreamsKafka = "akka-streams-kafka"
val akkaActorsKafka = "akka-actors-kafka"

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
      "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % sparkVersion
    )
  )

lazy val structuredStreaming = (project in file("structured-streaming"))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % sparkVersion,
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

