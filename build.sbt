
val sparkStreamingOld = "spark_streaming_old"
val sparkStreamingLatest = "spark_streaming_latest"

lazy val spark_streaming_old = (project in file(sparkStreamingOld))
  .settings(commonSettings(sparkStreamingOld): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.1.0",
      "org.apache.spark" %% "spark-streaming" % "2.1.0",
      "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"
    )
  )

lazy val spark_streaming_latest = (project in file(sparkStreamingLatest))
  .settings(commonSettings(sparkStreamingLatest): _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.1.0",
      "org.apache.spark" %% "spark-streaming" % "2.1.0",
      "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.1.0"
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

