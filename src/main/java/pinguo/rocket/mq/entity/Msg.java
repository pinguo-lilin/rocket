package pinguo.rocket.mq.entity;

public class Msg {
	private String appName;
	private String opcode;
	private String info;
	private float time;
	private String key;
	
	/**
	 * 
	 * 初始化发送消息参数
	 * 
	 * @param appName	系统名称
	 * @param opcode	操作码
	 * @param info		消息实体
	 * @param time		消息时间
	 * @param key		消息业务ID,比如订单ID、用户ID
	 */
	public Msg(String appName, String opcode, String info, float time, String key) {
		this.appName = appName;
		this.opcode = opcode;
		this.info = info;
		this.time = time;
		this.key = key;
	}
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public float getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
