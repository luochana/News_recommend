# News_recommend
本次项目是基于大数据计算引擎的新闻推荐系统--"今日小站"，包含了爬虫，新闻网站（前端和后端），推荐系统（Spark）。<br>
##前端效果：<br>
![](https://github.com/luochana/News_recommend/tree/master/pic/pic1.png) <br>
<br>
![](https://github.com/luochana/News_recommend/tree/master/pic/pic2.png) <br>
<br>
![](https://github.com/luochana/News_recommend/tree/master/pic/pic3.png) <br>
<br>
##一.爬虫 <br>
###开发环境： <br>
* pycharm+python3 <br>
###软件架构： <br>
* mysql+scrapy+splash <br>
###项目描述：<br>
爬虫项目负责重复爬取今日头条首页新闻，去除重复新闻，存入mysql。<br>
<br>
##二.新闻网站： <br>
###开发环境： <br>
*  IntelliJ IDEA + maven + git + linux <br>
###软件架构： <br>
* mysql + springboot <br>
###项目描述：<br>
今日小站是基于springboot框架搭建的web项目，用户在网站完成注册登录后，网站会记录用户的浏览行为。同时网站也会把推荐结果呈现给用户。<br>
<br>
##三.推荐系统： <br>
###开发环境： <br>
* IntelliJ IDEA + maven + git + linux <br>
###软件架构： <br>
* zookeeper + flume + kafka +  spark  + mysql<br>
###项目描述：<br>
推荐系统计算两两文章间的相似度，并将与之最相似的10条数据id存入mysql。实时处理用户行为日志，将结果存入mysql。根据用户行为数据对用户进行推荐，并将结果存入mysql。<br>
<br>
![](https://github.com/luochana/News_recommend/tree/master/pic/pic4.png) <br>
<br>

###服务器规划：<br>
* spark1:192.168.56.101 <br>
* spark2:192.168.56.102 <br>
* spark3:192.168.56.103 <br>
<br>
项目持续更新中....<br>
