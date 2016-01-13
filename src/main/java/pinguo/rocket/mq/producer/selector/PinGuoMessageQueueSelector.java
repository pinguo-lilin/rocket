package pinguo.rocket.mq.producer.selector;

import java.util.List;
import java.util.Random;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;

/**
 * producer发送消息，队列选择
 */
public class PinGuoMessageQueueSelector implements MessageQueueSelector {
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        String key = (String) arg;
        int generateId = 0;
        if (key == null || key.isEmpty()) {
            generateId = random.nextInt();
        } else {
            generateId = arg.hashCode();
        }

        if (generateId < 0) {
            generateId = Math.abs(generateId);
        }
        generateId = generateId % mqs.size();
        return mqs.get(generateId);
    }
}
