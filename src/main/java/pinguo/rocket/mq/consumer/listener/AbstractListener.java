package pinguo.rocket.mq.consumer.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.comm.HttpHelper;
import pinguo.rocket.mq.entity.Strategy;

public class AbstractListener {
	protected Map<String, Map<String, Strategy>> topicTagStrategys = new HashMap<String, Map<String, Strategy>>();
	
	protected boolean dispatherMessage(List<MessageExt> msgs){
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
		params.add(new BasicNameValuePair("appName", topic));
		params.add(new BasicNameValuePair("opcode", tag));
		params.add(new BasicNameValuePair("info", info));

		// 转发消息
		System.out.println("msg="+info);
		System.out.println("url="+url);
		System.out.println("timeOut="+timeOut);
		System.out.println("retryTimes"+retryTimes);
		
		boolean isSuccess = false;
		for (int i = 0; i < retryTimes; i++) {
			try {
				System.out.println("----------------------post");
//				String result = HttpHelper.post(url, params, timeOut);
				String result = "0";
				if(Integer.parseInt(result) == 1){
					isSuccess = true;
					break;
				}
			} catch (Exception e) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
				}
				continue;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
}
