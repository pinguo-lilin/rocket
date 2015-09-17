package pinguo.rocket.mq.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pinguo.rocket.mq.comm.PropertiesHelper;
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
	protected String namesrvAddr;
	protected String defaultEnv = "testing";
	protected String propertiesPath = "src/main/resources/config/" + this.defaultEnv + "/rmq.properties";
	protected Map<String, Consumer> consumers = new HashMap<String, Consumer>();
	protected Map<String, List<Subscribe>> subscribes = new HashMap<String, List<Subscribe>>();
	protected Map<String, Map<String, Map<String, Strategy>>> strategys = new HashMap<String, Map<String, Map<String, Strategy>>>();

	protected AbstractConsumer(){
		PropertiesHelper pHelper = new PropertiesHelper(this.propertiesPath);
		this.namesrvAddr = pHelper.getString("rmq.namesrvAddr");
	}
	
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

	public String getDefaultEnv() {
		return defaultEnv;
	}

	public void setDefaultEnv(String defaultEnv) {
		this.defaultEnv = defaultEnv;
	}
}
