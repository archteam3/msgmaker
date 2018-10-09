package team.three.msgmaker.producer;

import team.three.msgmaker.Config;
import team.three.msgmaker.Const;

public class ProducerFactory {
	private static ProducerFactory instance = null;
	
	public static void initialize() throws Exception {
		instance = new ProducerFactory();
		instance.connect();
	}
	
	public static void release() throws Exception {
		instance.disconnect();
	}
	
	public static IProducer get() {
		return instance.getProducer();
	}
	
	private Class cls = null;
	
	private ProducerFactory() { }

	private void connect() throws Exception {
		// 방식이 그닥이나... ㅠㅠ
		Config cfg = Config.get();
		String t = cfg.getTyp();
		if(  Const.KAFKA.equals(t) ) {
			ConnectionKafka.connect();
			cls = ProducerKafka.class;
		} else if ( Const.NACT.equals(t) ) {
			ConnectionNats.connect();
			cls = ProducerNATS.class;
		} else if ( Const.RABBITMQ.equals(t) ) {
			ConnectionRabbitMQ.connect();
			cls = ProducerRabbitMQ.class;
		}
	}

	private void disconnect() throws Exception {
		// 방식이 그닥이나... ㅠㅠ
		Config cfg = Config.get();
		String t = cfg.getTyp();
		if(  Const.KAFKA.equals(t) ) {
			ConnectionKafka.disconnect();
		} else if ( Const.NACT.equals(t) ) {
			ConnectionNats.disconnect();
		} else if ( Const.RABBITMQ.equals(t) ) {
			ConnectionRabbitMQ.disconnect();
		}
	}
	
	public IProducer getProducer() {
		IProducer ret = null;
		try{
			ret = (IProducer)cls.newInstance();
		} catch ( Exception ex ) {
			ret = null;
		}
		return ret;
	}
	
}
