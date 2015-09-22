package pinguo.rocket.mq.entity;

public class Strategy {
	private String url;
	private int timeOut = 300;
	private int retryTimes = 3;
	private int sendInterval = 10;
	
	public int getSendInterval() {
		return sendInterval;
	}
	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	
	@Override
	public String toString() {
		return "url=" + this.url + " timeOut=" + this.timeOut + " retryTimes=" + this.retryTimes + " sendInterval=" + this.sendInterval;
	}
}
