package pinguo.rocket.mq.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * 
 * 抽象consumer
 *
 */
public abstract class AbstractConsumer {
	protected String consumerName;
	protected String namesrvAddr = "10.1.2.236:9876";
	protected Map<String, Consumer> consumers = new HashMap<String, Consumer>();
	protected Map<String, List<Subscribe>> subscribes = new HashMap<String, List<Subscribe>>();
	protected Map<String, Map<String, Map<String, Strategy>>> strategys = new HashMap<String, Map<String, Map<String, Strategy>>>();

	public abstract void start();

	
	/*********************************** getter and setter ************************************/
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
}
