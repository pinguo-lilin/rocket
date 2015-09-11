package pinguo.rocket.mq.entity;

import java.util.Map;

/**
 * 
 * 返回数据格式
 *
 */
public class ResultStatus {
	private int status;
	private String message;
	private Map<String, Object> data;
	private long serverTime;

	/**
	 * 初始化
	 * 
	 * @param status	状态200=成功，其它失败
	 * @param message	内容
	 * @param data		数据
	 */
	public ResultStatus(int status, String message, Map<String, Object> data) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.serverTime = System.currentTimeMillis();
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	
	
}
