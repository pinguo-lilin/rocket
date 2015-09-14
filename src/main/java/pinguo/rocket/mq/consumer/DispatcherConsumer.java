package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import pinguo.rocket.mq.comm.ConsumerHelper;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
	public static void main(String[] args) throws DocumentException {
		if (args.length == 0) {
			return;
		}
		String consumerName = args[0];
		System.out.println(consumerName);
		
		String configPath = "src/main/resources/rocket.xml";
		ConsumerHelper consumerHelper = new ConsumerHelper(configPath);
		consumerHelper.parseXml();
		Map<String, Consumer> consumers = consumerHelper.getConsumers();
		Map<String, List<Subscribe>> subscribes = consumerHelper.getSubscribes();
		Map<String, Map<String, Map<String, Strategy>>> strategys = consumerHelper.getStrategys();
		
		System.out.println(strategys.get("cc_album").get("msg").get("userLogin").getTimeOut());
		
	}
}
