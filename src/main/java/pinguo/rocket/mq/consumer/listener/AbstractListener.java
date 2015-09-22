package pinguo.rocket.mq.consumer.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.comm.HttpHelper;
import pinguo.rocket.mq.comm.ResultCode;
import pinguo.rocket.mq.entity.Strategy;

public class AbstractListener {
	
	protected String consumerId = null;
	//日志初始化
	protected final static Logger logger = LoggerFactory.getLogger(AbstractListener.class);
	protected Map<String, Map<String, Strategy>> topicTagStrategys = new HashMap<String, Map<String, Strategy>>();
	
	protected boolean dispatherMessage(List<MessageExt> msgs){
		
		// consumerId
		this.consumerId = "["+UUID.randomUUID().toString()+"] ";
		
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
		int intervalTime = strategy.getSendInterval();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appName", topic));
		params.add(new BasicNameValuePair("opcode", tag));
		params.add(new BasicNameValuePair("info", info));

		logger.info(consumerId + "消费消息策略，"+strategy.toString());
		
		// 转发消息
		boolean isSuccess = false;
		try {
			for (int i = 0; i < retryTimes; i++) {

				long startTime = System.currentTimeMillis();
				String result = HttpHelper.post(url, params, timeOut);
				logger.trace(consumerId + "服务器路由返回数据，"+result);
				long endTime = System.currentTimeMillis();
				JSONObject jsonObject = JSONObject.parseObject(result);
				
				if (!jsonObject.containsKey("status")) {
					logger.trace(consumerId + "第" + (i + 1) + "次消费失败，服务器返回数据格式错误，result=" + result);
				} else if (jsonObject.getInteger("status") == ResultCode.SERVER_ERROR) {
					logger.trace(consumerId + "第" + (i + 1) + "次消费失败，服务器异常，返回" + ResultCode.SERVER_ERROR + "错误");
				} else if (jsonObject.getInteger("status") == ResultCode.SUCCESS) {
					isSuccess = true;
					float totalTime = (endTime - startTime) / 1000;// 单位秒
					logger.trace(consumerId + "消息在第" + (i + 1) + "次，消费成功，总共耗时" + totalTime + "秒");
					break;
				} else {
					logger.trace(consumerId + "第" + (i + 1) + "次消费失败，服务器返回status无法识别，status=" + jsonObject.getInteger("status"));
				}
				Thread.sleep(intervalTime);
			}
		} catch (ClientProtocolException clientProtocolException) {
			logger.error(consumerId + "httpPost异常，ClientProtocolException=" + clientProtocolException.getMessage());
		} catch (IOException ioException) {
			logger.error(consumerId + "httpPost异常，IOException=" + ioException.getMessage());
		} catch (HttpException httpException) {
			logger.error(consumerId + "httpPost异常，HttpException=" + httpException.getMessage());
		} catch (InterruptedException interruptedException) {
			logger.error(consumerId + "sleep异常，InterruptedException=" + interruptedException.getMessage());
		} catch (Exception exception){
			logger.error(consumerId + "其它异常，Exception=" + exception.getMessage());
		}
		
		return isSuccess;
	}
}
