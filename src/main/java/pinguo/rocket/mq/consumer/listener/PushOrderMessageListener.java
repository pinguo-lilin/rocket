package pinguo.rocket.mq.consumer.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.comm.HttpHelper;
import pinguo.rocket.mq.entity.Strategy;

public class PushOrderMessageListener implements MessageListenerOrderly{
	private Map<String, Map<String, Strategy>> topicTagStrategys = new HashMap<String, Map<String, Strategy>>();
	
	public PushOrderMessageListener(Map<String, Map<String, Strategy>> topicTagStrategys){
		this.topicTagStrategys = topicTagStrategys;
	}
	
	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
		MessageExt msgExt = msgs.get(0);
		String topic = msgExt.getTopic();
		String tag = msgExt.getTags();
		byte[] msg = msgExt.getBody();
		String info = new String(msg);
		Strategy strategy = this.topicTagStrategys.get(topic).get(tag);
		String url = strategy.getUrl();
		int timeOut = strategy.getTimeOut();
		int retryTimes = strategy.getRetryTimes();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("info", info));

		for (int i = 0; i < retryTimes; i++) {
			try {
				String jsonStr = HttpHelper.post(url, params, timeOut);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ConsumeOrderlyStatus.SUCCESS;
	}

}
