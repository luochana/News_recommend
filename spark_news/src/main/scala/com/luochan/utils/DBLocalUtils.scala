package com.luochan.utils

import java.sql.{Connection, DriverManager}
;

object DBLocalUtils {
  val url = "jdbc:mysql://localhost:3306/mydatabase?useUnicode=true&characterEncoding=utf-8"
  val username = "root"
  val password = "*******"

  classOf[com.mysql.jdbc.Driver]

  def getConnection(): Connection = {
    DriverManager.getConnection(url, username, password)
  }

  def close(conn: Connection): Unit = {
    try{
      if(!conn.isClosed() || conn != null){
        conn.close()
      }
    }
    catch {
      case ex: Exception => {
        ex.printStackTrace()
      }
    }
  }

}