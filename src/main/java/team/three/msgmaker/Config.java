package team.three.msgmaker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Config {
	private static Config instance = null;
	
	public static Config get() {
		if( instance == null ) {
			synchronized (Config.class) {
				if( instance == null ) {
					instance = new Config();
				}
			}
		}
		return instance;
	}

	public static final String usage = 
			"usage:\n" +
			"------------------------------------------------\n" + 
			"  [k|n|r] [start eq.id] [end eq.id] (option)\n" +
			"  -mc : message count default 10000\n" +
			"  -cf : config file default ./msgmaker.conf\n" + 
			"  -si : short message interval (ms) default 50ms\n" +
			"  -li : long message interval (ms) default 10000000ms = 10000s\n" +
			"  -ss : short message size (byte) default 500byte\n" +
			"  -ls : long message size (kbyte) default 3072kbyte"
			;
	

	private String typ;
	private int stIdx;
	private int edIdx;
	private long si;
	private long li;
	private int ss;
	private int ls;
	private int msgCnt;
	private String configFile;
	private Map<String, String> global;
	private Map<String, String> indiv;
	private Map<String, Object> opt;
	
	
	private Config() {
		global = new HashMap<String, String>();
		indiv = new HashMap<String, String>();
		opt = new HashMap<String, Object>();
		
		typ = "";
		stIdx = 0;
		edIdx = 0;
		si = 50;
		li = 10000000;
		ss = 500;
		ls = 1024 * 3 * 1024;
		msgCnt = 10000;
		configFile = "./msgmaker.conf";
	}
	
	public void setConfig(String[] args) throws Exception {
    	if( args.length >= 3 ) {
    		typ = args[0].toLowerCase(Locale.KOREAN);
    		stIdx = Integer.parseInt(args[1]);
    		edIdx = Integer.parseInt(args[2]);
    		
    		if( args.length > 3 ) {
    			int j = 3;
    			for( ; j<args.length; j++) {
    				if( "-si".equals(args[j])) {
    					si = Long.parseLong(args[++j]);
    				} else if( "-li".equals(args[j]) ) {
    					li = Long.parseLong(args[++j]);
    				} else if( "-ss".equals(args[j]) ) {
    					ss = Integer.parseInt(args[++j]);
    				} else if( "-ls".equals(args[j]) ) {
    					ls = Integer.parseInt(args[++j]) * 1024;
    				} else if( "-cf".equals(args[j]) ) {
    					configFile = args[++j];
    				} else if( "-mc".equals(args[j]) ) {
    					msgCnt = Integer.parseInt(args[++j]);
    				}
    			}
    		}
    		
    		if( typ.equals("k") ) {
    			typ = Const.KAFKA;
    		} else if ( typ.equals("n") ) {
    			typ = Const.NACT;
    		} else if ( typ.equals("r") ) {
    			typ = Const.RABBITMQ;
    		} else {
    			System.out.println("type is wrong ( only k, n, r available )");
    			return;
    		}
    		
    		if( stIdx < 0 || edIdx < 0 || stIdx > edIdx ) {
    			System.out.println("check index!");
    			return;
    		}
    		
    		if( si <= 0 || li <= 0 || ss <= 0 || ls <= 0 ) {
    			System.out.println("check interval or size!");
    			return;
    		}
    		
    	} else {
    		System.out.println(usage);
    		return;
    	}
    	
    	try( BufferedReader br = new BufferedReader( new FileReader(configFile))) {
    		String line;
    		String[] aTmp = null;
    		Map<String, String> tmp = null;
    		while( (line = br.readLine()) != null ) {
    			if( line.startsWith("[") ) {
    				line = line.split("#")[0].trim();
    				if( "[GLOBAL]".equals(line) ) {
    					tmp = this.global;
    				} else if( ("[" + typ + "]").equals(line) ) {
    					tmp = this.indiv;
    				} else {
    					tmp = null;
    				}
    			} else if ( line != null && line.length() > 0 ) {
    				if( tmp == null )
    					continue;
    				line = line.split("#")[0].trim();
    				aTmp = line.split("=");
    				if( aTmp.length >= 2 ) {
    					tmp.put(aTmp[0].trim(), aTmp[1].trim());
    				}
    			}
    		}
    	}
    	
    	print();
        
	}
	
	public void print() {
    	System.out.println("type : " + typ);
    	System.out.println("start index : " + stIdx);
    	System.out.println("end index : " + edIdx);
    	System.out.println("message count : " + msgCnt);
    	System.out.println("short message interval : " + si + " ms");
    	System.out.println("long message interval : " + li + " ms");
    	System.out.println("short message size : " + ss + " bytes");
    	System.out.println("long message size : " + (ls/1024) + " Kbytes");
    	System.out.println("Config file : " + configFile);
    	System.out.println("CONFIG FILE========================");
    	Iterator<String> keys = global.keySet().iterator();
    	while( keys.hasNext() ) {
    		String k = keys.next();
    		System.out.println("\t" + k + " = " + global.get(k));
    	}
    	System.out.println("\t[" + typ + "]");
    	keys = indiv.keySet().iterator();
    	while( keys.hasNext() ) {
    		String k = keys.next();
    		System.out.println("\t\t" + k + " = " + indiv.get(k));
    	}
	}
	
	

	public String getTyp() {
		return typ;
	}

	public int getStIdx() {
		return stIdx;
	}

	public int getEdIdx() {
		return edIdx;
	}

	public long getSi() {
		return si;
	}

	public long getLi() {
		return li;
	}

	public int getSs() {
		return ss;
	}

	public int getLs() {
		return ls;
	}

	public int getMsgCnt() {
		return msgCnt;
	}

	public Map<String, String> getGlobal() {
		return global;
	}

	public Map<String, String> getIndiv() {
		return indiv;
	}

	public Map<String, Object> getOpt() {
		return opt;
	}

	@Override
	public String toString() {
		return "Config [typ=" + typ + ", stIdx=" + stIdx + ", edIdx=" + edIdx + ", si=" + si + ", li=" + li + ", ss="
				+ ss + ", ls=" + ls + ", msgCnt=" + msgCnt + ", configFile=" + configFile + ", global=" + global
				+ ", indiv=" + indiv + ", opt=" + opt + "]";
	}
	
	
}
