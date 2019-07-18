package com.luochan.recommend

import java.sql.Connection

import com.luochan.caseclass.newsLog
import com.luochan.utils.DBLocalUtils
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.SparkSession
import scala.collection.mutable

/*
    用协同过滤算法为用户推荐5条新闻
*/


object UserCF {
  def parseRating(str:String):newsLog={
    val fields=str.split(",")
    assert(fields.size==4)
    newsLog(fields(0).toInt,fields(1).toInt,fields(2).toDouble,fields(3).toString)
  }
  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("UserCF").master("local").getOrCreate()
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf-8")
      .option("dbtable", "newsLogs")
      .option("user", "root")
      .option("password", "*********")
      .load()

    jdbcDF.show()
    val tempRatings=jdbcDF.rdd.map(x=>{
      newsLog(x(0).asInstanceOf[Int],x(1).asInstanceOf[Int],x(2).asInstanceOf[Double],x(3).asInstanceOf[String])
    })

    val ratings=spark.createDataFrame(tempRatings).toDF("userId","newsId","rating","timeStamp")
    val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))
    val alsExplicit = new ALS().setMaxIter(5).setRegParam(0.01).setUserCol("userId"). setItemCol("newsId").setRatingCol("rating")
    val modelExplicit = alsExplicit.fit(training)
    val predictionsExplicit = modelExplicit.transform(test)
    val usersRecommend=modelExplicit.recommendForAllUsers(5)
    predictionsExplicit.show(false)
    usersRecommend.show(false)
    var connection:Connection = null
    connection=DBLocalUtils.getConnection()
    usersRecommend.collect().foreach(x=>{
    //  println(x.getClass)
    //  println(x.toString())
      val userId=x.get(0).asInstanceOf[Int]
      val recommend=x.get(1).asInstanceOf[mutable.WrappedArray[Float]].toIterator
      var recommendStr=""
      while(recommend.hasNext) {
        var itemStr=recommend.next().toString.split(",")(0).substring(1)
         recommendStr = recommendStr + itemStr+ ","
      }
      println(recommendStr.substring(0,recommendStr.length-1))
      resultInsertIntoMysql(userId,recommendStr.substring(0,recommendStr.length-1))
    })
      DBLocalUtils.close(connection)

    def resultInsertIntoMysql(userId:Int,recommendStr:String): Unit ={
      try{
        val sql="insert into userCF(userId,recommendStr) values(?,?) on duplicate key update recommendStr=?"
        val pst=connection.prepareStatement(sql)
        pst.setInt(1, userId)
        pst.setString(2,recommendStr)
        pst.setString(3,recommendStr)
        pst.execute()
      }
      catch{
        case e=>e.printStackTrace()
      }
    }
  }
}