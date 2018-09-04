package com.hello.riskControl

import com.hello.constants.Constants
import com.hello.riskControl.drools._
import com.hello.riskControl.util.DroolsParser
import com.hello.utils.PropertiesLoad._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.immutable.Map


object RiskRule {
  Logger.getLogger("org").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  val log = org.apache.log4j.LogManager.getLogger("RiskRule")

  def main(args: Array[String]): Unit = {
    loadFilePath()
    var topic = getString(Constants.KAFKA_HTTP_TOPICS) //metrics-userGw-https   metrics-userGw-tcp

    var hour = 4
    var minute = 0
    var second = 0

    if (args.length == 1) {
      topic = args(0)
    }
    if (args.length == 3) {
      hour = Integer.parseInt(args(0))
      minute = Integer.valueOf(args(1))
      second = Integer.valueOf(args(2))
    }
    if (args.length == 4) {
      topic = args(0)
      hour = Integer.parseInt(args(1))
      minute = Integer.valueOf(args(2))
      second = Integer.valueOf(args(3))
    }

    var k = 10

    val conf = new SparkConf().setAppName(RiskRule.getClass.getSimpleName)
//      .setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //      .set("spark.streaming.concurrentJobs", "3")//默认为1
      .set("spark.streaming.kafka.consumer.cache.enabled", "false") //关闭线程安全
//      .set("spark.streaming.backpressure.enabled", "true") //开启反压机制,会影响性能
      .set("spark.streaming.backpressure.initialRate", "2000") //限制第一次批处理应该消费的数据
      .set("spark.streaming.kafka.maxRatePerPartition", "4000") //每秒钟从topic的每个partition最多消费的数据
      .set("spark.streaming.stopGracefullyOnShutdown", "true")


    val kafkaParams = Map(
      //      "bootstrap.servers" -> getString(Constants.METADATA_BROKER_SOAINFOR_LIST),
      "bootstrap.servers" -> getString(Constants.METADATA_BROKER_GATEWAY_LIST),
      "group.id" -> getString(Constants.KAFKA_USERGW_GROPID),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      //      "auto.offset.reset" -> "earliest",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    //    val topic = getString(Constants.KAFKA_SOAINFOR_TOPICS)//metrics-userGw-https   metrics-userGw-tcp
    val group = getString(Constants.KAFKA_USERGW_GROPID)
    val ssc = new StreamingContext(conf, Seconds(k)) //时间处理窗口
    ssc.sparkContext.setLogLevel("WARN")


    val stream: InputDStream[ConsumerRecord[String, String]] = createStream(kafkaParams, conf, ssc, topic)

    stream.foreachRDD(rdd => {

      println("本次RDD大小：" + rdd.count())
      rdd.filter(_ != null).foreachPartition(iter => {
          DroolsUtil.updateRule()
        while (iter.hasNext) {
          try {

            DroolsParser.parseData_dymanic(iter.next().value())

          } catch {
            case ex: Exception => ex.printStackTrace();
          }
        }

      })


    })

    ssc.start()
    stopByMarkFile(ssc)
    ssc.awaitTermination()

  }

  def createStream(kafkaParam: Map[String, Object],
                   conf: SparkConf, ssc: StreamingContext, topics: String): InputDStream[ConsumerRecord[String, String]] = {

    var kafkaStream: InputDStream[ConsumerRecord[String, String]] = null

    //      val topicSet = topics.split(",").toSet
    kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics.split(","), kafkaParam)
    )
    //08版的消费kafka的api
    //      val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topic)

    kafkaStream
  }

  /**
    * 通过一个消息文件来定时触发是否需要关闭流程序
    *
    * @param ssc StreamingContext
    */
  def stopByMarkFile(ssc: StreamingContext): Unit = {
    val intervalMills = 10 * 1000 // 每隔10秒扫描一次消息是否存在
    var isStop = false
    val hdfs_file_path = getString(Constants.HDFS_RISKCONTROL_FILE_PATH) //判断消息文件是否存在，如果存在就
    while (!isStop) {
      isStop = ssc.awaitTerminationOrTimeout(intervalMills)
      if (!isStop && isExistsMarkFile(hdfs_file_path)) {
        Thread.sleep(2000)
        ssc.stop(stopSparkContext = true, stopGracefully = true)
      }
    }
  }


  /**
    * 判断是否存在mark file
    *
    * @param hdfs_file_path mark文件的路径
    * @return
    */
  def isExistsMarkFile(hdfs_file_path: String): Boolean = {
    val conf = new Configuration()
    val path = new Path(hdfs_file_path)
    val fs = path.getFileSystem(conf)
    fs.exists(path)
  }

}
