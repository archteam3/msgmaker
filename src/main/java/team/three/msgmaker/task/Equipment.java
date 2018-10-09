package team.three.msgmaker.task;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

import team.three.msgmaker.Config;

public class Equipment {
	private String eqpId;
	private int currMsgIdx;
	private int longMsgIndicator;
	
	public Equipment(int eqpIdx) {
		Config cfg = Config.get();
		
		eqpId = "EQP-" + StringUtils.leftPad(Integer.toString(eqpIdx), 4, '0');
		currMsgIdx = 1;
		int lmi = (int)(cfg.getLi() / cfg.getSi());
		Random r = new Random(System.nanoTime());
		longMsgIndicator = r.nextInt(lmi - 1) + 1;
	}

	public void addIdx() {
		int ret = currMsgIdx;
		currMsgIdx++;
		longMsgIndicator++;
	}
	
	public String getEqpId() {
		return eqpId;
	}

	public int getCurrMsgIdx() {
		return currMsgIdx;
	}

	public int getLongMsgIndicator() {
		return longMsgIndicator;
	}
	
	public void setLongMsgIndicator() {
		longMsgIndicator = 0;
	}
	
	
}
