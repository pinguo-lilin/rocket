package pinguo.rocket.mq;

import java.util.List;
import java.util.Map;

public class PgConsumer {

	private List<Map<String, String>> subscribes = null;
	
	public PgConsumer(List<Map<String, String>> subscribes) {
		this.subscribes = subscribes;
	}

	/**
	 * 订阅专题
	 * 
	 * @return boolean
	 */
	public boolean consumer() {
		return true;
	}

	public List<Map<String, String>> getSubscribes() {
		return subscribes;
	}

	public void setSubscribes(List<Map<String, String>> subscribes) {
		this.subscribes = subscribes;
	}
	
}
