import caseclass.newsLog

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time



object HotNewsStat {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("key.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")

    val inputStream = env.addSource( new FlinkKafkaConsumer[String]("first", new SimpleStringSchema(), properties) )
    inputStream.map( line => {
      var userLog:newsLog = null
      val fields = line.split(",")
      try {
        if (fields.length == 4) {
          userLog = newsLog(fields(0).toInt, fields(1).toInt, fields(2).toDouble, fields(3).trim.toLong)
        }
      } catch {
        case e => e.printStackTrace()
      }
      userLog
    }).assignAscendingTimestamps(_.timeStamp * 1000)
      .keyBy(_.newsId)
      .timeWindow(Time.hours(1),Time.minutes(5))




  }

}
