package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import pinguo.rocket.mq.comm.XmlHelper;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
	public static void main(String[] args) throws DocumentException {
		if (args.length == 0) {
			System.out.println("没有初始化consumer");
			return;
		}
		String contextEnv = "testing";// 运行环境每次打包切换
		String consumerName = args[0];// 第一个参数默认consumerName
		String configPath = "src/main/resources/rocket.xml";// rmq配置文件
		
		//解析xml
		XmlHelper xmlHelper = new XmlHelper(configPath);
		xmlHelper.parseXml();
		Map<String, Consumer> consumers = xmlHelper.getConsumers();
		Map<String, List<Subscribe>> subscribes = xmlHelper.getSubscribes();
		Map<String, Map<String, Map<String, Strategy>>> strategys = xmlHelper.getStrategys();

		//条件过滤
		if (!consumers.containsKey(consumerName) || !subscribes.containsKey(consumerName)) {
			System.out.println("没有满足条件的consumer");
			return;
		}
		
		//消费
		AbstractConsumer pushConsumer = new PushConsumer(consumerName);
		
		pushConsumer.setDefaultEnv(contextEnv);
		pushConsumer.setConsumers(consumers);
		pushConsumer.setSubscribes(subscribes);
		pushConsumer.setStrategys(strategys);
		pushConsumer.start();
		System.out.println("consumer启动成功......");
	}
}
