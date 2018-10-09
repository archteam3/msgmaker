package team.three.msgmaker.producer;

public interface IProducer {
	public void initialize() throws Exception ;
	public void send(String topic, String msg) throws Exception ;
	public void finalize() throws Exception;
}
