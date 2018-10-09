package team.three.msgmaker;

import java.nio.file.Path;
import java.nio.file.Paths;

import team.three.msgmaker.producer.IProducer;
import team.three.msgmaker.producer.ProducerFactory;


public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	Path currentRelativePath = Paths.get("");
    	String s = currentRelativePath.toAbsolutePath().toString();
    	System.out.println("Current relative path is: " + s);

    	Config conf = Config.get();
    	conf.setConfig(args);
    	
    	ProducerFactory.initialize();
    	IProducer pr = ProducerFactory.get();
    	System.out.println(pr);
    	ProducerFactory.release();
    }
}
