package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

import pinguo.rocket.mq.comm.ConsumerHelper;
import pinguo.rocket.mq.consumer.listener.PushOrderMessageListener;
import pinguo.rocket.mq.consumer.listener.PushOrdinaryMessageListener;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * 
 *PushConsumer逻辑
 *
 */
public class PushConsumer extends ConsumerFactory{

	public PushConsumer(String consumerName) {
		this.consumerName = consumerName;
	}
	
	@Override
	public void start() throws MQClientException {
		Consumer consumerModel = consumers.get(consumerName);
		List<Subscribe> subscribeList = subscribes.get(consumerName);
		
		//初始化
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerName);
		consumer = (DefaultMQPushConsumer) ConsumerHelper.objectPropertiesToOtherOne(consumerModel, consumer);
		
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.setNamesrvAddr(namesrvAddr);
		
		//订阅专题
		for (Subscribe subscribe : subscribeList) {
			consumer.subscribe(subscribe.getTopic(), subscribe.getTags());
		}
		
		//注册回调监听器
		Map<String, Map<String, Strategy>> topicTagStrategys = this.strategys.get(consumerName);
		
		//顺序消息
		if(consumerModel.isOrder()){
			MessageListenerOrderly messageListener = new PushOrderMessageListener(topicTagStrategys);
			consumer.registerMessageListener(messageListener);
		//无序消息
		}else{
			MessageListenerConcurrently messageListener = new PushOrdinaryMessageListener(topicTagStrategys);
			consumer.registerMessageListener(messageListener);
		}
		
		consumer.start();
	}

}
