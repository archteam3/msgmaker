package team.three.msgmaker.producer;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import team.three.msgmaker.Config;

public class ProducerKafka implements IProducer {
	private Producer<String, byte[]> producer;

	@Override
	public void initialize()  throws Exception  {
		// TODO Auto-generated method stub
		Config cfg = Config.get();
		Properties props = new Properties();
		
		Map<String, String> cMap = cfg.getIndiv();
		Iterator<String> keys = cMap.keySet().iterator();
		while( keys.hasNext() ) {
			String key = keys.next();
			props.put(key, cMap.get(key));
		}
		
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		
		producer = new KafkaProducer<>(props);
	}

	@Override
	public void send(String topic, String msg) throws Exception  {
		// TODO Auto-generated method stub
		RecordMetadata res = producer.send(new ProducerRecord<String, byte[]>(topic, msg.getBytes())).get();
	}

	@Override
	public void finalize() throws Exception {
		producer.close();
	}
}
