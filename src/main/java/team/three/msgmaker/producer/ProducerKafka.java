package team.three.msgmaker.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import team.three.msgmaker.Config;

public class ProducerKafka implements IProducer {
	private Producer<String, byte[]> producer;

	@Override
	public void initialize()  throws Exception  {
		// TODO Auto-generated method stub
		Config cfg = Config.get();
		Properties props = new Properties();
		props.put("bootstrap.servers", Config.get().getIndiv().get("server_ip"));
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		
		producer = new KafkaProducer<>(props);
	}

	@Override
	public void send(String topic, String msg) throws Exception  {
		// TODO Auto-generated method stub
		producer.send(new ProducerRecord<String, byte[]>(topic, msg.getBytes()));
	}

	@Override
	public void finalize() throws Exception {
		producer.close();
	}
}
