# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
#import pymongo
import pymysql

class ToutiaoPipeline(object):
    def __init__(self):
        self.connect = pymysql.Connect(
            host='120.78.173.225',
            port=3306,
            user='root',
            passwd='luochen1998',
            db='news_db',
            charset='utf8'
        )
        self.cursor = self.connect.cursor()

    def open_spider(self,spider):
        pass

    def process_item(self, item, spider):
        print(item['title'])
        search_last_sql="SELECT id FROM news ORDER BY id DESC LIMIT 1"
        sql="INSERT INTO news (id,title,source_url,Abstract,source,tag,chinese_tag,news_class,content) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)"
        try:
            if item['articleTitle']!='' and item['articleContent']!='':
                self.cursor.execute(search_last_sql);
                id=self.cursor.fetchone();
                if id is None:
                    id=0
                else:
                    id=id[0]
                print(id)
                self.cursor.execute(sql,[id+1,item['title'],item['source_url'],item['abstract'],item['source'],item['tag'],item['chinese_tag'],item['news_class'],item['articleContent']])
                self.connect.commit()
                print("数据已插入！")
        except:
            print("数据重复！")

    def close_spider(self, spider):
        self.cursor.close()
        self.connect.close()
    # def __init__(self,mongo_url,mongo_db):
    #     pass
    #     # self.mongo_url=mongo_url
    #     # self.mongo_db=mongo_db
    #
    # @classmethod
    # def from_crawler(cls,crawler):
    #     pass
    #     # return cls(
    #     #     mongo_url=crawler.settings.get("MONGO_URL"),
    #     #     mongo_db=crawler.settings.get("MONGODB_DATABASE")
    #     # )
    #
    # def open_spider(self, spider):
    #     pass
    #     # self.client = pymongo.MongoClient(self.mongo_url)
    #     # self.db = self.client[self.mongo_db]
    #
    #
    # def process_item(self, item, spider):
    #     # sheet=self.db[item['news_class']]
    #     # if sheet.find_one({'source_url':item['source_url']}):
    #     #     print('数据已存在')
    #     # else:
    #     #     sheet.insert(dict(item))
    #     #
    #     # return item
    #     pass
    #
    # def close_spider(self, spider):
    #     pass
    #     #self.client.close()
