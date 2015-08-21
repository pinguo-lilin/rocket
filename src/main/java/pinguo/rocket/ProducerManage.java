package pinguo.rocket;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

public class ProducerManage {
	private static DefaultMQProducer mqProducer = null;

	private ProducerManage() {
	}

	public static DefaultMQProducer getProducer() {
		// 初始化
		if (mqProducer == null) {
			DefaultMQProducer producer = new DefaultMQProducer("");
			producer.setNamesrvAddr("");

			mqProducer = producer;
		}
		return mqProducer;
	}
}
