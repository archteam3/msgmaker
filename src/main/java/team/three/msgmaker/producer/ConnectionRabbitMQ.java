package team.three.msgmaker.producer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import team.three.msgmaker.Config;
import team.three.msgmaker.Const;

public class ConnectionRabbitMQ {
	public static void connect() throws Exception {
		Config cfg = Config.get();
		
		ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(cfg.getIndiv().get("server_ip"));
        factory.setUsername(cfg.getIndiv().get("id"));
        factory.setPassword(cfg.getIndiv().get("pw"));
        
        Connection connection = factory.newConnection();
        
        cfg.getOpt().put(Const.CONNECTION, connection);
	}
	
	public static void disconnect() throws Exception {
		Config cfg = Config.get();
		Connection connection = (Connection)cfg.getOpt().get(Const.CONNECTION);
		connection.close();
	}
}
