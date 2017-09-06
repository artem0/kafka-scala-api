package com.example.spec

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkSpec extends BeforeAndAfterAll {
  this: Suite =>

  private var sparkContext: SparkContext = _

  override def beforeAll(): Unit = {
    super.beforeAll()

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName(this.getClass.getSimpleName)

    sparkConfig.foreach { case (k, v) => conf.setIfMissing(k, v) }

    sparkContext = new SparkContext(conf)
  }

  def sparkConfig: Map[String, String] = Map.empty

  override def afterAll(): Unit = {
    if (sparkContext != null) {
      sparkContext.stop()
      sparkContext = null
    }
    super.afterAll()
  }

  def sc: SparkContext = sparkContext

}