package com.luochan.streaming

import java.sql.Connection

import com.luochan.caseclass.newsLog
import com.luochan.utils.DBLocalUtils
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils


/*
用户的浏览记录：
log4j->flume->kafka->spark streaming->mysql
*/


object KafkaConsumer {

  def main(args:Array[String]):Unit={

    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(20))
    val topicsSet = Set("test")
    val brokers = "192.168.56.101:9092,192.168.56.102:9092,192.168.56.103:9092"
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringDecoder")
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    val messagesDStream = messages.map(_._2).map(line => {
      println(line)
      var userLog: newsLog = null
      if (line != null) {
        try {
          val fields = line.split(",")
          if (fields.length == 4) {
            userLog = newsLog(fields(0).toInt, fields(1).toInt, fields(2).toFloat,fields(3))
          }
        } catch {
          case e => e.printStackTrace()
        }
      }
      userLog
    })

    val userLog=messagesDStream.map{
      case newsLog(userId,newsId,rating,timeStamp)=>{
        ((userId,newsId,rating),timeStamp)
      }
    }.reduceByKey{(timeStamp1,timeStamp2)=>{
      if(timeStamp1>timeStamp2)
        timeStamp1
      else
        timeStamp2
    }
    }

    var connection:Connection = null
    userLog.foreachRDD(rdd=>rdd.collect().foreach(x=>{
      resultInsertIntoMysql(x._1._1,x._1._2,x._1._3,x._2)
    }))

    ssc.start()
    ssc.awaitTermination()

    def resultInsertIntoMysql(userId:Int,newsId:Int,rating:Float,timeStamp:String): Unit ={
      try{
        connection=DBLocalUtils.getConnection()
        val sql="insert into newsLogs values(?,?,?,?)"
        val pst=connection.prepareStatement(sql)
        pst.setInt(1, userId)
        pst.setInt(2, newsId)
        pst.setFloat(3,rating)
        pst.setString(4,timeStamp)
        pst.execute()
        DBLocalUtils.close(connection)
      }
      catch{
        case e=>e.printStackTrace()
      }
    }
  }
}




