package pinguo.rocket.mq.consumer.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.entity.Strategy;

/**
 * 
 * PushConsumer顺序消息监听器
 *
 */
public class PushOrdinaryMessageListener extends AbstractListener implements MessageListenerConcurrently{
	
	public PushOrdinaryMessageListener(Map<String, Map<String, Strategy>> topicTagStrategys){
		this.topicTagStrategys = topicTagStrategys;
	}
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		this.dispatherMessage(msgs);
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
