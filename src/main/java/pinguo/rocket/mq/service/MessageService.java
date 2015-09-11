package pinguo.rocket.mq.service;

import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

import pinguo.rocket.mq.comm.ApplicationContextUtil;
import pinguo.rocket.mq.comm.ResultCode;
import pinguo.rocket.mq.entity.Msg;
import pinguo.rocket.mq.entity.ResultStatus;
import pinguo.rocket.mq.producer.selector.PinGuoMessageQueueSelector;

/**
 * 
 * 发送消息逻辑
 *
 */
@Service
public class MessageService {

	/**
	 * 发送消息
	 * 
	 * @param  Msg msg
	 * @return ResultStatus
	 */
	public ResultStatus send(Msg msg) {
		String topic = msg.getAppName();
		String tag = msg.getOpcode();
		String body = msg.getInfo();
		String key = msg.getKey();

		// 发送消息
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		Message message = new Message(topic, tag, key, body.getBytes());

		try {
			SendResult sendResult = producer.send(message, new PinGuoMessageQueueSelector(), key);
			if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
				return new ResultStatus(ResultCode.SUCCESS, "消息发送成功", null);
			}
			return new ResultStatus(ResultCode.HANDLER_ERROR, "发送失败，sendStatus=" + sendResult.getSendStatus(), null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultStatus(ResultCode.SERVER_ERROR, "服务器异常，error=" + e.getMessage(), null);
		}
	}
}
