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
public class PushOrdinaryMessageListener implements MessageListenerConcurrently{
	private Map<String, Map<String, Strategy>> topicTagStrategys = new HashMap<String, Map<String, Strategy>>();
	
	public PushOrdinaryMessageListener(Map<String, Map<String, Strategy>> topicTagStrategys){
		this.topicTagStrategys = topicTagStrategys;
	}
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		// 消息属性
		MessageExt msgExt = msgs.get(0);
		String topic = msgExt.getTopic();
		String tag = msgExt.getTags();
		byte[] msg = msgExt.getBody();
		String info = new String(msg);
		
		// 转发策略信息
		Strategy strategy = this.topicTagStrategys.get(topic).get(tag);
		String url = strategy.getUrl();
		int timeOut = strategy.getTimeOut();
		int retryTimes = strategy.getRetryTimes();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("info", info));

		// 转发消息
		System.out.println("msg="+info);
		System.out.println("url="+url);
		System.out.println("timeOut="+timeOut);
		System.out.println("retryTimes"+retryTimes);
		
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
