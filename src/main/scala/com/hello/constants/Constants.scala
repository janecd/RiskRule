package com.hello.constants

object Constants {

  val METADATA_BROKER_SOAINFOR_LIST = "metadata.broker.soainfo.list"
  val KAFKA_SOAINFOR_TOPICS="kafka.soainfo.topics"
  val KAFKA_SOAINFOR_GROPID="kafka.soainfo.groupid"
  val KAFKA_USERGW_GROPID="kafka.metrics-userGw.groupid"

  val KAFKA_USERGW_HOUR="kafka.metrics-userGw.hour"
  val KAFKA_USERGW_MINUTE="kafka.metrics-userGw.minute"
  val KAFKA_USERGW_SECOND="kafka.metrics-userGw.second"

  val METADATA_BROKER_GATEWAY_LIST ="metadata.broker.gateway.list"
  val KAFKA_HTTP_TOPICS="kafka.gateway.topics.http"




  //elasticsearch
  val ES_NDOES = "es.ndoes"
  val ES_PORT = "es.port"

  //zookeeper Cluster
  val ZOOKEEPER_LIST = "zookeeper.list"


  //spark task param
  val HDFS_RISKCONTROL_FILE_PATH = "hdfs.riskcontrol.file.path"



  //指标监控部分
  val red_envelopes="red_envelopes"   //红包提现
  val alipay_free_secret_withholding="alipay_free_secret_withholding"  //支付宝免密代扣
  val alipay_free_board="alipay_free_board"  //支付宝免登
  val regular_cards="regular_cards"    //常规购卡
  val exempted_cards="exempted_cards"  //免押购卡
  val certificationNA="certificationNA" //没有芝麻免押信用
  val certificationN="certificationN" //芝麻免押认证不满足的
  val certificationY="certificationY" //芝麻免押认证满足的
  val balance_refunds="balance_refunds" //余额充值
  val deposit_refunds="deposit_refunds" //押金充值
  val ebike_giveback_riding_success="ebike_giveback_riding_success" //城市电单车请求成功
  val ebike_giveback_riding_failed="ebike_giveback_riding_failed"  //城市电单车请求失败
  val earlytime="earlytime"   //这批数据最早时间
  val lattertime="lattertime"   //批量数据最新时间
  //val timestamp="timestamp"  //执行批次时间

  //以下是关键字部分
  val condition="condition"
  val entityMata="entityMata"
  val metric="metric"
  val Order_recordPay="Order.recordPay"
  val chargeType="chargeType"
  val AliPay_User_CheckZMXYScore="AliPay.User.CheckZMXYScore"
  val isAdmittance="isAdmittance"

  val red_envelopes_sql="select count(1) as red_envelopes  from table where Metric='ride.award.withdraw'"
  val alipay_free_secret_withholding_sql="select count(1) as alipay_free_secret_withholding  from table where Metric='Order.recordPay' and chargeType='45'"
  val alipay_free_board_sql="select count(1) as  alipay_free_board from table where Metric='AliPay.User.ShareInfo'"
  val regular_cards_sql="select count(1) as regular_cards from table where Metric='Order.recordPay' and chargeType='20'"
  val exempted_cards_sql="select count(1) as exempted_cards from table where Metric='Order.recordPay' and chargeType='24'"
  val certificationNA_sql="select count(1) as certificationNA from table where Metric='AliPay.User.CheckZMXYScore' and isAdmittance='N-A'"
  val certificationN_sql="select count(1) as certificationN from table where Metric='AliPay.User.CheckZMXYScore' and isAdmittance='N'"
  val certificationY_sql="select count(1) as certificationY from table where Metric='AliPay.User.CheckZMXYScore' and isAdmittance='Y'"
  val balance_refunds_sql="select count(1) as balance_refunds  from table where Metric='Order.recordPay' and chargeType='1'"
  val deposit_refunds_sql="select count(1) as deposit_refunds  from table where Metric='Order.recordPay' and chargeType='0'"
  val ebike_giveback_riding_success_sql="select count(1)  as ebike_giveback_riding_success from table where Metric='ebike_giveback_riding_success'"
  val ebike_giveback_riding_failed_sql="select count(1) as ebike_giveback_riding_failed from table where Metric='ebike_giveback_riding_failed'"
  //val lattertime_sql="select datetime from table where time=(select max(time) as time from table) "
  //val earlytime_sql="select datetime from table where time=(select min(time) as time from table) "
  val lattertime_sql="select max(time) as lattertime from table "
  val earlytime_sql="select min(time) as earlytime from table "
  val http_avg_sql="select action,count(action) as count,avg(logValue) as avgtime from table group by action,logValue"
  def getSql(fieldname:String):String={
    fieldname match {
      case Constants.red_envelopes => red_envelopes_sql
      case Constants.alipay_free_secret_withholding =>alipay_free_secret_withholding_sql
      case Constants.alipay_free_board => alipay_free_board_sql
      case Constants.regular_cards =>regular_cards_sql
      case Constants.exempted_cards =>exempted_cards_sql
      case Constants.certificationNA =>certificationNA_sql
      case Constants.certificationN =>certificationN_sql
      case Constants.certificationY => certificationY_sql
      case Constants.balance_refunds =>balance_refunds_sql
      case Constants.deposit_refunds => deposit_refunds_sql
      case Constants.ebike_giveback_riding_success => ebike_giveback_riding_success_sql
      case Constants.ebike_giveback_riding_failed =>ebike_giveback_riding_failed_sql
      case Constants.earlytime=>earlytime_sql
      case Constants.lattertime=>lattertime_sql
      case _ =>"没有匹配到合适的类型"
    }

  }




}
