package team.three.msgmaker.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import team.three.msgmaker.Config;
import team.three.msgmaker.Const;

public class ProducerRabbitMQ implements IProducer {
	private Channel channel;
	private String ex;
	
	@Override
	public void initialize() throws Exception {
		// TODO Auto-generated method stub
		Config cfg = Config.get();
		Connection con = (Connection)cfg.getOpt().get(Const.CONNECTION);
		channel = con.createChannel();
		ex = cfg.getIndiv().get("exchange_name");
		channel.exchangeDeclare(ex, "topic");
		/*
		channel.addShutdownListener(new ShutdownListener() {
			
			@Override
			public void shutdownCompleted(ShutdownSignalException cause) {
				// TODO Auto-generated method stub
				cause.printStackTrace();
			}
		});
		*/
	}

	@Override
	public void send(String topic, String msg) throws Exception  {
		// TODO Auto-generated method stub
		// @TODO properties 검토 필요
		channel.basicPublish(ex, topic, null, msg.getBytes());
	}
	
	@Override
	public void finalize() throws Exception {
		channel.close();
	}
}
