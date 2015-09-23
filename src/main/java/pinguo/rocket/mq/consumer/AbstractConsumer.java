package pinguo.rocket.mq.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.consumer.MQConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;

import pinguo.rocket.mq.comm.UtilHelper;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * 
 * 抽象consumer
 *
 */
public abstract class AbstractConsumer {
	protected MQConsumer consumer;
	protected String consumerName;

	public String getConsumerName() {
		return consumerName;
	}

	protected String namesrvAddr;
	protected Map<String, Consumer> consumers = new HashMap<String, Consumer>();
	protected Map<String, List<Subscribe>> subscribes = new HashMap<String, List<Subscribe>>();
	protected Map<String, Map<String, Map<String, Strategy>>> strategys = new HashMap<String, Map<String, Map<String, Strategy>>>();

	protected AbstractConsumer(MQConsumer consumer) {
		this.namesrvAddr = UtilHelper.getProperties().getNamesrvAddr();
		this.consumer = consumer;
	}

	public abstract void start() throws MQClientException;

	/***********************************
	 * getter and setter
	 ************************************/
	public Map<String, Consumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(Map<String, Consumer> consumers) {
		this.consumers = consumers;
	}

	public Map<String, List<Subscribe>> getSubscribes() {
		return subscribes;
	}

	public void setSubscribes(Map<String, List<Subscribe>> subscribes) {
		this.subscribes = subscribes;
	}

	public Map<String, Map<String, Map<String, Strategy>>> getStrategys() {
		return strategys;
	}

	public void setStrategys(Map<String, Map<String, Map<String, Strategy>>> strategys) {
		this.strategys = strategys;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}
}
