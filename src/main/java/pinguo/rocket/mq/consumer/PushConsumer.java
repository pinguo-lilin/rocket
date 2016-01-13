package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;

import pinguo.rocket.mq.comm.ReflectionUtils;
import pinguo.rocket.mq.consumer.listener.PushOrderMessageListener;
import pinguo.rocket.mq.consumer.listener.PushOrdinaryMessageListener;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * PushConsumer逻辑
 */
public class PushConsumer extends AbstractConsumer {

    private final static Logger logger = LoggerFactory.getLogger(PushConsumer.class);

    public PushConsumer(String consumerName) {
        super();
        this.consumerName = consumerName;
    }

    @Override
    public void start() throws MQClientException {
        Consumer consumerModel = consumers.get(consumerName);
        List<Subscribe> subscribeList = subscribes.get(consumerName);

        // 初始化
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerName);
        consumer = (DefaultMQPushConsumer) ReflectionUtils.objectPropertiesToOtherOne(consumerModel, consumer);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(namesrvAddr);

        System.out.println("persit=" + consumer.getPersistConsumerOffsetInterval());
        // 不是push?
        System.out.println("pullBatchSize=" + consumer.getPullBatchSize());
        System.out.println("namesrvAddr=" + consumer.getNamesrvAddr());

        logger.info("consumer=" + consumerName + "启动参数 parmas=" + consumer.toString());
        // 订阅专题
        for (Subscribe subscribe : subscribeList) {
            consumer.subscribe(subscribe.getTopic(), subscribe.getTags());
        }

        // 转发策略初始化
        Map<String, Map<String, Strategy>> topicTagStrategys = this.strategys.get(consumerName);

        // 顺序消息
        if (consumerModel.getOrder()) {
            MessageListenerOrderly messageListener = new PushOrderMessageListener(topicTagStrategys);
            consumer.registerMessageListener(messageListener);
            logger.trace("consumer=" + consumerName + "顺序消费已启动...");
            // 普通消息
        } else {
            MessageListenerConcurrently messageListener = new PushOrdinaryMessageListener(topicTagStrategys);
            consumer.registerMessageListener(messageListener);
            logger.trace("consumer=" + consumerName + "普通消费已启动...");
        }
        consumer.start();
    }
}
