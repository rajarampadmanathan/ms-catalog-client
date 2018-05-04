package com.example.demo.receiver;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.CatalogClientApplication;
import com.google.gson.Gson;

@Component
public class UserUpdateReceiver {


	private static final Logger LOG=LoggerFactory.getLogger(UserUpdateReceiver.class);
	//This is not the best practice. just testing.
	@Autowired
	CatalogClientApplication client;
	
	@RabbitListener(queues = { "test-queue" })
	public void userUpdateListener(Message message) {
		System.out.println(new String(message.getBody()));
		Gson gson=new Gson();
		Map<String,Object> m=gson.fromJson(new String(message.getBody()), java.util.Map.class);
		client.invalidateUser(m.get("userId").toString());

	}
	
	@KafkaListener(topics= {"test"})
	public void userUpdateListener(ConsumerRecord<String, String> consumerRecord) {
		LOG.info("Kafka Listener received a message:"+consumerRecord.toString() +" from topic -"+consumerRecord.topic());
	}
}
