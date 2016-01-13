package pinguo.rocket.mq.consumer.listener;

import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import pinguo.rocket.mq.entity.Strategy;

/**
 * PushConsumer顺序消息监听器
 */
public class PushOrdinaryMessageListener extends AbstractListener implements MessageListenerConcurrently {

    public PushOrdinaryMessageListener(Map<String, Map<String, Strategy>> topicTagStrategys) {
        this.topicTagStrategys = topicTagStrategys;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        boolean result = this.dispatherMessage(msgs);
        if (result) {
            logger.trace(this.consumerId + "消息消费成功，msgs=" + msgs.toString());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        logger.error(this.consumerId + "消息消费失败,稍后重试，msgs=" + msgs.toString());
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
