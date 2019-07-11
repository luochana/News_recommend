package com.luochan.tools

import java.text.{DateFormat, SimpleDateFormat}
import java.util
import java.util.Date

import org.ansj.domain
import org.ansj.splitWord.analysis.ToAnalysis

object RecommendKits {
  def getCurrentTime(): Date=
  {
    val df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //设置日期格式
  val date=df.format(new Date())
    val getNewsTime=df.parse(date)
    return getNewsTime
  }

  def splitWordToSeq(news: String) = {
    val terms: util.List[domain.Term] = ToAnalysis.parse(news).getTerms
    val size = terms.size()
    var res = ""
    for (i <- 0 until size) {
      res += terms.get(i.toInt).getName + " "
    }
    res.split(" ")
  }

}
