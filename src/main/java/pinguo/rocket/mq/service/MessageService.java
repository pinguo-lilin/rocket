package pinguo.rocket.mq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import pinguo.rocket.mq.comm.ApplicationContextUtil;
import pinguo.rocket.mq.entity.Msg;
import pinguo.rocket.mq.exception.ProducerException;
import pinguo.rocket.mq.producer.selector.PinGuoMessageQueueSelector;

/**
 * 
 * 发送消息逻辑
 *
 */
@Service
public class MessageService {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	/**
	 * 发送消息
	 * 
	 * @param  Msg msg
	 * @return boolean
	 * @throws InterruptedException 
	 * @throws MQBrokerException 
	 * @throws RemotingException 
	 * @throws MQClientException 
	 * @throws ProducerException 
	 */
	public boolean send(Msg msg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException, ProducerException {
		String topic = msg.getAppName();
		String tag = msg.getOpcode();
		String body = msg.getInfo();
		String key = msg.getKey();

		// 发送消息
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		Message message = new Message(topic, tag, key, body.getBytes());

		SendResult sendResult = producer.send(message, new PinGuoMessageQueueSelector(), key);
		if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
			logger.info("发送成功，SendStatus=" + sendResult.getSendStatus() + " message=" + message.toString() + " info=" + new String(message.getBody()));
			return true;
		}
		throw new ProducerException("发送失败，sendStatus=" + sendResult.getSendStatus());
	}
}
