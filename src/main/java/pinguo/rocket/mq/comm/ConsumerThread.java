package pinguo.rocket.mq.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;

import pinguo.rocket.mq.consumer.AbstractConsumer;
import pinguo.rocket.mq.init.Rocketmq;

public class ConsumerThread implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);

	private AbstractConsumer consumer;

	public ConsumerThread(AbstractConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void run() {
		try {
			consumer.start();
			logger.trace("消费者，consumerName=" + this.consumer.getConsumerName() + "线程已启动...");
		} catch (MQClientException e) {
			logger.error("消费者，consumerName=" + this.consumer.getConsumerName() + "线程启动失败");
		}
	}
}
