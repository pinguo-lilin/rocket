package pinguo.rocket.mq.consumer.listener;

import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.entity.Strategy;

/**
 * 
 * PushConsumer无序消息监听器
 *
 */
public class PushOrderMessageListener extends AbstractListener implements MessageListenerOrderly {
	public PushOrderMessageListener(Map<String, Map<String, Strategy>> topicTagStrategys) {
		this.topicTagStrategys = topicTagStrategys;
	}

	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
		boolean result = this.dispatherMessage(msgs);
		return ConsumeOrderlyStatus.SUCCESS;
	}
}
