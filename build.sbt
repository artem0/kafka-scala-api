val sparkStreamingOld = "spark-streaming-old"
val sparkStreamingLatest = "spark-streaming-latest"
val akkaStreamsKafka = "akka-streams-kafka"

val sparkVersion = "2.1.0"
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

lazy val akkaKafkaModule = (project in file(akkaStreamsKafka))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= sparkCommon ++ Seq(
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.17"
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
    Resolver.sonatypeRepo("releases")
  )
)

