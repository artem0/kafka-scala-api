package com.example.avro

import org.scalatest._
import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}

class GenericAvroCodecsSpecification extends WordSpec with Matchers {
  val testSchema = new Schema.Parser().parse("""{
                                                   "type":"record",
                                                   "name":"FiscalRecord",
                                                   "namespace":"avro",
                                                   "fields":[
                                                      {
                                                         "name":"calendarDate",
                                                         "type":"string"
                                                      },
                                                      {
                                                         "name":"fiscalWeek",
                                                         "type":[
                                                            "int",
                                                            "null"
                                                         ]
                                                      },
                                                      {
                                                         "name":"fiscalYear",
                                                         "type":[
                                                            "int",
                                                            "null"
                                                         ]
                                                      }
                                                   ]
                                                }""")

  "Generic Avro codec" should {

    "Round trip generic record using Generic Injection" in {
      implicit val genericInjection = GenericAvroCodecs[GenericRecord](testSchema)
      val testRecord = buildGenericAvroRecord(("2012-01-01", 1, 12))
      val bytes = Injection[GenericRecord, Array[Byte]](testRecord)
      val attempt = Injection.invert[GenericRecord, Array[Byte]](bytes)
      assert(attempt.get == testRecord)
    }

    "Round trip generic record using Binary Injection" in {
      implicit val genericBinaryInjection = GenericAvroCodecs.toBinary[GenericRecord](testSchema)
      val testRecord = buildGenericAvroRecord(("2012-01-01", 1, 12))
      val bytes = Injection[GenericRecord, Array[Byte]](testRecord)
      val attempt = Injection.invert[GenericRecord, Array[Byte]](bytes)
      assert(attempt.get == testRecord)
    }

    "Round trip generic record using Json Injection" in {
      implicit val genericJsonInjection = GenericAvroCodecs.toJson[GenericRecord](testSchema)
      val testRecord = buildGenericAvroRecord(("2012-01-01", 1, 12))
      val jsonString = Injection[GenericRecord, String](testRecord)
      val attempt = Injection.invert[GenericRecord, String](jsonString)
      assert(attempt.get == testRecord)
    }
  }

  def buildGenericAvroRecord(i: (String, Int, Int)): GenericRecord = {

    val fiscalRecord = new GenericData.Record(testSchema)
    fiscalRecord.put("calendarDate", i._1)
    fiscalRecord.put("fiscalWeek", i._2)
    fiscalRecord.put("fiscalYear", i._3)
    fiscalRecord
  }
}