package pinguo.rocket.mq;

public class PgProducer {

	private String topic = "";
	private String tag = "";
	private String body = "";
	private String key = "";
	
	public PgProducer(String topic, String tag, String body){
		this.topic = topic;
		this.tag = tag;
		this.body = body;
	}
	
	public PgProducer(String topic, String tag, String body, String key){
		this.topic = topic;
		this.tag = tag;
		this.body = body;
		this.key = key;
	}
	
	/**
	 * 发送MQ消息
	 * 
	 * @return boolean
	 */
	public boolean send() {

		return true;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
