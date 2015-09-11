package pinguo.rocket.mq.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

import pinguo.rocket.mq.comm.ApplicationContextUtil;

public class Rocketmq implements InitializingBean{
    private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);
    
	/**
	 * spring所有bean初始化完后执行
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("begin start init rocket mq...debug");
		logger.error("begin start init rocket mq...error");
		logger.info("begin start init rocket mq...info");
		
		//初始化producer
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		producer.start();
		
		logger.debug("producer init complete,nameAddr=" + producer.getNamesrvAddr());
	}

}
