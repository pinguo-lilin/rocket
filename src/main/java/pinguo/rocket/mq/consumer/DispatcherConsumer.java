package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import pinguo.rocket.mq.comm.ConsumerXmlHelper;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
	public static void main(String[] args) throws DocumentException {
		if (args.length == 0) {
			return;
		}
		String consumerName = args[0];
		String configPath = "src/main/resources/rocket.xml";
		ConsumerXmlHelper consumerHelper = new ConsumerXmlHelper(configPath);
		consumerHelper.parseXml();
		Map<String, Consumer> consumers = consumerHelper.getConsumers();
		Map<String, List<Subscribe>> subscribes = consumerHelper.getSubscribes();
		Map<String, Map<String, Map<String, Strategy>>> strategys = consumerHelper.getStrategys();

		if (!consumers.containsKey(consumerName) || !subscribes.containsKey(consumerName)) {
			return;
		}
		
		ConsumerFactory pushConsumer = new PushConsumer(consumerName);
		pushConsumer.setConsumers(consumers);
		pushConsumer.setSubscribes(subscribes);
		pushConsumer.setStrategys(strategys);
		
		try {
			pushConsumer.start();
		} catch (Exception e) {
			
		}
	}
}
