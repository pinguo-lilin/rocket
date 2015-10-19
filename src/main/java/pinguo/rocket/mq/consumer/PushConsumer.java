package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.MQConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

import pinguo.rocket.mq.comm.ReflectionUtils;
import pinguo.rocket.mq.consumer.listener.PushOrderMessageListener;
import pinguo.rocket.mq.consumer.listener.PushOrdinaryMessageListener;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * 
 * PushConsumer逻辑
 *
 */
public class PushConsumer extends AbstractConsumer {

	private final static Logger logger = LoggerFactory.getLogger(PushConsumer.class);

	public PushConsumer(MQConsumer consumer) {
		super(consumer);
	}

	@Override
	public void start() throws MQClientException {
		Consumer consumerModel = consumers.get(consumerName);
		List<Subscribe> subscribeList = subscribes.get(consumerName);

		DefaultMQPushConsumer pushConsumer = (DefaultMQPushConsumer) consumer;

		// 初始化
		pushConsumer = (DefaultMQPushConsumer) ReflectionUtils.objectPropertiesToOtherOne(consumerModel, pushConsumer);

		pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		pushConsumer.setNamesrvAddr(namesrvAddr);

		pushConsumer.setConsumeMessageBatchMaxSize(1);
		System.out.println("persit=" + pushConsumer.getPersistConsumerOffsetInterval());
		System.out.println("pullBatchSize=" + pushConsumer.getPullBatchSize());
		System.out.println("namesrvAddr2=" + pushConsumer.getNamesrvAddr());

		logger.info("consumer=" + consumerName + "启动参数 parmas=" + consumer.toString());
		// 订阅专题
		for (Subscribe subscribe : subscribeList) {
			pushConsumer.subscribe(subscribe.getTopic(), subscribe.getTags());
		}

		// 转发策略初始化
		Map<String, Map<String, Strategy>> topicTagStrategys = this.strategys.get(consumerName);

		// 顺序消息
		if (consumerModel.getOrder()) {
			MessageListenerOrderly messageListener = new PushOrderMessageListener(topicTagStrategys);
			pushConsumer.registerMessageListener(messageListener);
			logger.trace("consumer=" + consumerName + "顺序消费已启动...");
			// 普通消息
		} else {
			MessageListenerConcurrently messageListener = new PushOrdinaryMessageListener(topicTagStrategys);
			pushConsumer.registerMessageListener(messageListener);
			logger.trace("consumer=" + consumerName + "普通消费已启动...");
		}
		pushConsumer.start();
	}
}
