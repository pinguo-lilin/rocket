package pinguo.rocket.mq.entity;

public class Consumer {

	private String name;
	private String type;
	private boolean order;
	private int persistConsumerOffsetInterval;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPersistConsumerOffsetInterval() {
		return persistConsumerOffsetInterval;
	}
	public void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
		this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
	}
	public boolean isOrder() {
		return order;
	}
	public void setOrder(boolean order) {
		this.order = order;
	}
}
