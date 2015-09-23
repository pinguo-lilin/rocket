package pinguo.rocket.mq.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

import pinguo.rocket.mq.comm.ApplicationContextUtil;
import pinguo.rocket.mq.comm.BeanManage;
import pinguo.rocket.mq.comm.ConsumerThread;
import pinguo.rocket.mq.comm.XmlHelper;
import pinguo.rocket.mq.consumer.AbstractConsumer;
import pinguo.rocket.mq.consumer.PushConsumer;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class Rocketmq implements InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);

	/**
	 * spring所有bean初始化完后执行
	 */
	@Override
	public void afterPropertiesSet() {

		// 初始化配置信息
		String classPath = this.getClass().getResource("/").getPath();
		String configPath = classPath + "/rocket.xml";
		XmlHelper xmlHelper = new XmlHelper(configPath);
		xmlHelper.parseXml();
		Map<String, Consumer> consumers = xmlHelper.getConsumers();
		Map<String, List<Subscribe>> subscribes = xmlHelper.getSubscribes();
		Map<String, Map<String, Map<String, Strategy>>> strategys = xmlHelper.getStrategys();

		// 初始化producer
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		try {
			producer.start();
			logger.trace("producer=PinGuoProducer，启动成功...");
		} catch (MQClientException e) {
			logger.error("producer=PinGuoProducer，启动失败...");
		}
		
		logger.info("消费者数据，consumers=" + consumers.toString()+" subscribes=" + subscribes.toString()+" strategys=" + strategys.toString());
		
		Set<String> consumerNames = consumers.keySet();
		for (String consumerName : consumerNames) {
			Boolean isCreate = ApplicationContextUtil.contain(consumerName);
			if (isCreate) {
				logger.trace("消费者已经创建，consumerName="+consumerName);
				continue;
			}
			
			if(!subscribes.containsKey(consumerName) || !strategys.containsKey(consumerName)){
				logger.error("消费者订阅或策略配置错误，请确认后重启");
			}
			
			Consumer consumerEntity = consumers.get(consumerName);
			AbstractConsumer consumer = null;
			if (consumerEntity.getType() == Consumer.TYPE_PUSH) {
				BeanManage.addConsumerBeanToFactory(DefaultMQPushConsumer.class, consumerName, new HashMap<>());
				DefaultMQPushConsumer consumerBean = (DefaultMQPushConsumer) ApplicationContextUtil.getBean(consumerName);
				consumer = new PushConsumer(consumerBean);
				logger.trace("消费者，consumerName=" + consumerName + " 类型是" + consumerBean.toString());
			} else {
				BeanManage.addConsumerBeanToFactory(DefaultMQPushConsumer.class, consumerName, new HashMap<>());
				DefaultMQPushConsumer consumerBean = (DefaultMQPushConsumer) ApplicationContextUtil.getBean(consumerName);
				consumer = new PushConsumer(consumerBean);
				logger.trace("消费者，consumerName=" + consumerName + " 类型是" + consumerBean.toString());
			}

			consumer.setConsumerName(consumerName);
			consumer.setConsumers(consumers);
			consumer.setSubscribes(subscribes);
			consumer.setStrategys(strategys);

			ConsumerThread consumerThread = new ConsumerThread(consumer);
			Thread thread = new Thread(consumerThread);
			thread.start();
		}
	}

}
