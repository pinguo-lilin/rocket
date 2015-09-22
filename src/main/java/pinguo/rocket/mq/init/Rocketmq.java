package pinguo.rocket.mq.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

import pinguo.rocket.mq.comm.ApplicationContextUtil;

public class Rocketmq implements InitializingBean{
    private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);
    
	/**
	 * spring所有bean初始化完后执行
	 */
	@Override
	public void afterPropertiesSet() {
		// 初始化producer
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		try {
			producer.start();
			logger.trace("producer=PinGuoProducer，启动成功...");
		} catch (MQClientException e) {
			logger.error("producer=PinGuoProducer，启动失败...");
		}
	}

}
