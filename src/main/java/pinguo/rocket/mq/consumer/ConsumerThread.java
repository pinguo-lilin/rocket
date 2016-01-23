package pinguo.rocket.mq.consumer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pinguo.rocket.mq.consumer.monitor.ConsumerMonitor;
import pinguo.rocket.mq.init.Rocketmq;

import java.util.Map;

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
            logger.error("Occur client error with message=" + e.getMessage());
        }
    }

    public void create(ConsumerMonitor consumerMonitor, String consumerName) throws IllegalStateException {
        Thread thread = new Thread(this);
        thread.start();
        String tcName = thread.getName();
        consumerMonitor.putThread(tcName, thread);

        logger.trace("consumer=" + consumerName + ",threadName=" + tcName + "已经启动...");
    }
}

