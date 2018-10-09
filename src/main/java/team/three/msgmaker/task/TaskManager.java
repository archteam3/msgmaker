package team.three.msgmaker.task;

import java.util.ArrayList;
import java.util.List;

import team.three.msgmaker.Config;

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
		
		
		for(int i=0; i<thdCnt; i++ ) {
			
		}
	}
}
