package pinguo.rocket;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class Producer {
	String appName = "";
	String opCode = "";
	String info = "";
	int time = 0;
	String key = "";
	
	public boolean send() {
		DefaultMQProducer producer = ProducerManage.getProducer();
		try {
			producer.start();
			
			String topic = "";
			String tag = "";
			byte[] msgBytes = this.info.getBytes();
			
			Message msg = new Message(topic, tag, msgBytes);
			
			SendResult result = producer.send(msg);
			SendStatus sendStatus = result.getSendStatus();
			
			if(sendStatus == SendStatus.SEND_OK){
				return true;
			}
			return false;
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (MQBrokerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
