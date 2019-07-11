package com.luochan.recommend

import java.sql.Connection
import breeze.linalg.min
import com.luochan.tools.RecommendKits
import com.luochan.utils.DBLocalUtils
import org.apache.spark.ml.feature.HashingTF
import org.apache.spark.ml.feature.IDF
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, MatrixEntry, RowMatrix}
import org.apache.spark.sql.SparkSession

object ItemSimilarity {

  def transposeRowMatrix(m: RowMatrix): RowMatrix = {
    val indexedRM = new IndexedRowMatrix(m.rows.zipWithIndex.map({
      case (row, idx) => new IndexedRow(idx, row)}))
    val transposed = indexedRM.toCoordinateMatrix().transpose.toIndexedRowMatrix()
    new RowMatrix(transposed.rows
      .map(idxRow => (idxRow.index, idxRow.vector))
      .sortByKey().map(_._2))
  }

  def main(arges: Array[String]): Unit= {

    val spark = SparkSession.builder().appName("ItemSimilarity").master("local").getOrCreate()
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf-8")
      .option("dbtable", "news")
      .option("user", "root")
      .option("password", "*********")
      .load()

    jdbcDF.show(100,false)
    //分词,并将每条新闻的内容转换为特征向量
    val Rdd = jdbcDF.rdd.map(x => (RecommendKits.splitWordToSeq(x(8).toString).toList))
    val hashingTF = new HashingTF().setInputCol("articleContent").setOutputCol("rawFeatures").setNumFeatures(2000)
    val temp = spark.createDataFrame(Rdd).toDF("articleContent")
    val featurizedData = hashingTF.transform(temp)
    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    val idfModel = idf.fit(featurizedData)
    val rescaledData = idfModel.transform(featurizedData)
    val rows = rescaledData.select("features").rdd.map(x => x(0).asInstanceOf[SparseVector].toDense).map(org.apache.spark.mllib.linalg.Vectors.fromML)
    val mat = new RowMatrix(rows)
    val mat1 = transposeRowMatrix(mat).rows.map(x => x.asInstanceOf[org.apache.spark.mllib.linalg.SparseVector].toDense).map(x => x.asInstanceOf[org.apache.spark.mllib.linalg.Vector])
    val mat3 = new RowMatrix(mat1)

    //计算两两向量间的余弦相似度
    val simsEstimate = mat3.columnSimilarities().entries.map { case MatrixEntry(row: Long, col: Long, sim: Double) => (row, col, sim) }
    val dff = spark.createDataFrame(simsEstimate).toDF("item_from", "item_to", "si")
    dff.show(false)

    //为每条新闻选取相似度最高的10条新闻
    dff.createOrReplaceTempView("test")
    dff.cache()
    var connection: Connection = null
    try
    {
      connection=DBLocalUtils.getConnection()
      val statement = connection.createStatement
      val temp = statement.executeQuery("SELECT newsId FROM ItemSim order by newsId desc limit 1")
      var LastRecord=0
      while(temp.next())
      {
        LastRecord=temp.getInt(1)+1
      }
      DBLocalUtils.close(connection)
      val num=jdbcDF.count()-1
      println("num is :"+num)
      println("LastRecord is :"+LastRecord)
      var strList = List.empty[String]
      for(i<- 0 to num.toString.toInt)
      {
        println("i is :"+i)
        val str="select * from test where item_from="+i.toString+" or item_to="+i.toString+" order by si desc limit 10"
        val tempDF=dff.sqlContext.sql(str).collect().toList
        println(tempDF.toString())
        val flag=min(tempDF.length,10)-1
        var simNews:String=new String()
        for(j<- 0 to flag.toString.toInt)
        {
          println("j is :"+j)
          if(i.toString.equals(tempDF.apply(j).get(0).toString))
            simNews=simNews+(tempDF.apply(j).get(1).asInstanceOf[Long].toString.toInt+LastRecord).toString+","
          else
            simNews=simNews+(tempDF.apply(j).get(0).asInstanceOf[Long].toString.toInt+LastRecord).toString+","
        }
        var timeStamp2=RecommendKits.getCurrentTime().toString
        var str1="{\"sim\":"+"\""+simNews+ "\""+","+"\"timeStamp\":"+"\""+timeStamp2+"\""+","+"\"id1\":"+"\""+(i+LastRecord).toString+"\""+"}"
        strList=strList :+str1
        println("strList is :"+strList.toString())
      }

      import spark.implicits._
      val rddData = spark.sparkContext.parallelize(strList)
      val resultDF = spark.read.json(rddData.toDS)
      val newsDF= jdbcDF.withColumn("id1",jdbcDF("id")+LastRecord)
      connection=DBLocalUtils.getConnection()
      newsDF.join(resultDF,newsDF("id1")===resultDF("id1"),"inner").select(newsDF("id1"),newsDF("source_url"),newsDF("tag"),newsDF("articleContent"),newsDF("articleTitle"),resultDF("sim"),resultDF("timeStamp")).collect().foreach(x=>{
        resultInsertIntoMysql(x(0).asInstanceOf[Int],x(1).asInstanceOf[String],x(2).asInstanceOf[String],x(3).asInstanceOf[String],x(4).asInstanceOf[String],x(5).asInstanceOf[String],x(6).asInstanceOf[String])
      })
      DBLocalUtils.close(connection)

      def resultInsertIntoMysql(newsId:Int,source_url:String,tag:String,articleContent:String,articleTitle:String,sim:String,timeStamp:String): Unit ={
        try{
          val sql="insert into ItemSim values(?,?,?,?,?,?,?)"
          val pst=connection.prepareStatement(sql)
          pst.setInt(1, newsId)
          pst.setString(2, source_url)
          pst.setString(3,tag)
          pst.setString(4,articleContent)
          pst.setString(5,articleTitle)
          pst.setString(6,sim)
          pst.setString(7,timeStamp)
          pst.execute()
        }
        catch{
          case e=>e.printStackTrace()
        }
      }
    }
    catch{
      case e=>e.printStackTrace()
    }
  }

}