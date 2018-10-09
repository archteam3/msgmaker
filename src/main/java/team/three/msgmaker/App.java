package team.three.msgmaker;

import java.nio.file.Path;
import java.nio.file.Paths;

import team.three.msgmaker.producer.ProducerFactory;
import team.three.msgmaker.task.TaskManager;


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

    	TaskManager tm = new TaskManager();
    	tm.start();
    	tm.join();
    	ProducerFactory.release();
    }
}
