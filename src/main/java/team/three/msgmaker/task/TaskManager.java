package team.three.msgmaker.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import team.three.msgmaker.Config;
import team.three.msgmaker.producer.ProducerFactory;

public class TaskManager extends Thread{
	private List<ThreadWorker> wkrs;
	
	public TaskManager() {
		wkrs = new ArrayList<>();
		
		Config cfg = Config.get();
		
		int sidx = cfg.getStIdx();
		int eidx = cfg.getEdIdx();
		int tpe = Integer.parseInt(cfg.getGlobal().get("eqp_cnt_per_thread"));
		
		int thdCnt = (eidx - sidx) / tpe;
		int remain = (eidx - sidx) % tpe;
		if( remain != 0 )
			thdCnt =+ 1;
		
		int end = sidx - 1;
		for(int i=0; i<thdCnt; i++ ) {
			sidx = end + 1;
			if( sidx + tpe > eidx ) {
				end = eidx;
			} else {
				end = sidx + tpe - 1;
			}
			wkrs.add(new ThreadWorker(
					i,
					sidx,
					end,
					ProducerFactory.get()));
		}
	}
	
	public void run() {
		long startTm = System.currentTimeMillis();
		for( ThreadWorker wkr : wkrs ) {
			wkr.start();
		}
		
		boolean isRun = true;
		int totalLongMsgCnt = 0;
		int totalShortMsgCnt = 0;
		while(isRun) {
			isRun = false;
			totalShortMsgCnt = 0;
			totalLongMsgCnt = 0;
			for( ThreadWorker wkr : wkrs ) {
				totalShortMsgCnt += wkr.getShortCnt();
				totalLongMsgCnt += wkr.getLongCnt();
				isRun = isRun | wkr.isRun();
			}
			System.out.println(
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss")) +
					" : Total:" + StringUtils.leftPad(Integer.toString(totalShortMsgCnt + totalLongMsgCnt), 12) +
					" ShortMsg:"+ StringUtils.leftPad(Integer.toString(totalShortMsgCnt), 12) +
					" LongMsg:"+ StringUtils.leftPad(Integer.toString(totalLongMsgCnt), 12)
					);
			if( isRun ) {
				try {
					Thread.sleep(10000);
				} catch( Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("========== END ====== test time : " + ((System.currentTimeMillis() - startTm)/1000) + " s");
	}
}
