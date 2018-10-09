package team.three.msgmaker.task;

import java.util.ArrayList;
import java.util.List;

import team.three.msgmaker.Config;
import team.three.msgmaker.Message;
import team.three.msgmaker.producer.IProducer;

public class ThreadWorker extends Thread {
	
	private int shortMsgCnt;
	private int longMsgCnt;
	private long interval;
	private List<Equipment> eqps;
	private Object lock;
	private int maxCnt;
	
	private int longMsgThreshold;
	private IProducer producer;
	
	public ThreadWorker(int startIdx, int endIdx, IProducer producer) {
		shortMsgCnt = 0;
		longMsgCnt = 0;
		
		Config cfg = Config.get();
		interval = cfg.getSi();
		
		longMsgThreshold = (int)(cfg.getLi() / cfg.getSi() );
		
		eqps = new ArrayList<>(endIdx - startIdx + 1);
		for( int i=startIdx; i<= endIdx; i++) {
			eqps.add(new Equipment(i));
		}
		
		lock = new Object();
		maxCnt = cfg.getMsgCnt();
		this.producer = producer;
	}
	
	public int getShortCnt() {
		synchronized (lock) {
			return this.shortMsgCnt;
		}
	}
	
	public int getLongCnt() {
		synchronized (lock) {
			return this.longMsgCnt;
		}
	}
	
	public void run() {
		boolean isRun = true;
		boolean isOver = true;
		Message msgMaker = Message.get();
		int si = 0;
		int li = 0;
		long tm = 0;
		while(isRun) {
			tm = System.currentTimeMillis();
			si = 0;
			li = 0;
			isOver = true;
			for( Equipment eqp : eqps ) {
				if( eqp.getCurrMsgIdx() > maxCnt) {
					continue;
				}
				isOver = isOver & false; 
				try {
					producer.send(
							eqp.getEqpId(), 
							msgMaker.getShortMsg(eqp.getEqpId(), eqp.getCurrMsgIdx())
					);
					eqp.addIdx();
					si++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for( Equipment eqp : eqps ) {
				if( eqp.getCurrMsgIdx() > maxCnt) {
					continue;
				}
				isOver = isOver & false; 
				if( eqp.getLongMsgIndicator() >= longMsgThreshold) {
					try {
						producer.send(
								eqp.getEqpId(), 
								msgMaker.getLongMsg(eqp.getEqpId(), eqp.getCurrMsgIdx())
						);
						eqp.addIdx();
						eqp.setLongMsgIndicator();
						li++;
					} catch ( Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			synchronized (lock) {
				this.shortMsgCnt += si;
				this.longMsgCnt += li;
			}
			tm = interval - (System.currentTimeMillis() - tm);
			if( tm > 0 ) {
				try {
					Thread.sleep(tm);
				} catch ( Exception ex ) {
					ex.printStackTrace();
				}
			} else {
				System.out.println("### Over interval ###");
			}
			
			if( isOver ) {
				isRun = false;
			}
		}
	}
	
}
