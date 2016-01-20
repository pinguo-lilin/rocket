package pinguo.rocket.mq.comm;

import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pinguo.rocket.mq.consumer.AbstractConsumer;
import pinguo.rocket.mq.init.Rocketmq;

/**
 * ConsumerThread
 */
public class ConsumerThread implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);

    private AbstractConsumer pushConsumer;

    public ConsumerThread(AbstractConsumer pushConsumer) {
        this.pushConsumer = pushConsumer;
    }

    @Override
    public void run() {
        try {
            pushConsumer.start();
        } catch (MQClientException e) {

        }
    }
}

