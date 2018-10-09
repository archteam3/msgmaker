package team.three.msgmaker;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class Message {
	private static Message instance = null;
	public static Message get() {
		if( instance == null ) {
			synchronized (Message.class) {
				if( instance == null ) {
					instance = new Message();
				}
			}
		}
		return instance;
	}
	
	// Message 구조
	// 21자리 생성시각(long) System.nanoTime() 사용
	// 10자리 : 설비 ID
	// 10자리 : 발생 seq
	// 이후 message로 구성
	private String shortBase;
	private String longBase;
	
	private Message() {
		Config cfg = Config.get();
		
		// make shortMessage Base
		shortBase = RandomStringUtils.randomAlphanumeric(cfg.getSs() - 41);
		longBase = RandomStringUtils.randomAlphanumeric(cfg.getLs() - 41);
	}
	
	public String getShortMsg(String eqpId, int index) {
		return
				StringUtils.rightPad(Long.toString(System.nanoTime()), 21) +
				StringUtils.rightPad(eqpId, 10) +
				StringUtils.rightPad(Integer.toString(index), 10) +
				shortBase;
	}
	
	public String getLongMsg(String eqpId, int index) {
		return
				StringUtils.rightPad(Long.toString(System.nanoTime()), 21) +
				StringUtils.rightPad(eqpId, 10) +
				StringUtils.rightPad(Integer.toString(index), 10) +
				longBase;
	}
}
