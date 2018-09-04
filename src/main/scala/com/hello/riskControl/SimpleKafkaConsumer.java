package com.hello.riskControl;

import com.hello.riskControl.drools.DroolsUtil;
import org.kie.api.runtime.KieSession;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class SimpleKafkaConsumer {
    public static void main(String[] args)  throws ClassNotFoundException,SQLException,UnsupportedEncodingException {

//        Properties props = new Properties();
//
//        props.put("bootstrap.servers", "10.111.80.4:9092,10.111.80.6:9092,10.111.80.5:9092");
//        //每个消费者分配独立的组号
//        props.put("group.id", "test-liuce");
//
//        //如果value合法，则自动提交偏移量
//        props.put("enable.auto.commit", "true");
//
//        //设置多久一次更新被消费消息的偏移量
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("auto.offset.reset", "latest");
//
//        //设置会话响应的时间，超过这个时间kafka可以选择放弃消费或者消费下一条消息
//        props.put("session.timeout.ms", "30000");
//
//        props.put("key.deserializer",
//                "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer",
//                "org.apache.kafka.common.serialization.StringDeserializer");
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//
//        consumer.subscribe(Collections.singletonList("gw-https"));
//
//        System.out.println("Subscribed to topic " + "gw-https");
//        int i = 0;
//
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records)
//
//                // print the offset,key and value for the consumer records.
//                System.out.printf("offset = %d, key = %s, value = %s\n",
//                        record.offset(), record.key(), record.value());
//        }
        KieSession kSession = DroolsUtil.getKieSession();
//        Message message1 = new Message();
//        message1.setStatus(1);
//        message1.setMsg("hello world!");
//        kSession.insert(message1);
//
//        kSession.fireAllRules();
    }
}
