package pinguo.rocket.mq.consumer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pinguo.rocket.mq.consumer.monitor.ConsumerMonitor;
import pinguo.rocket.mq.init.Rocketmq;

/**
 * ConsumerThread
 */
public class ConsumerThread implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Rocketmq.class);
    private boolean started = true;
    private AbstractConsumer pushConsumer;

    public ConsumerThread(AbstractConsumer pushConsumer) {
        this.pushConsumer = pushConsumer;
    }

    @Override
    public void run() {
        try {
            pushConsumer.start();
        } catch (MQClientException e) {
            this.started = false;
            logger.error("Start thread occur MQClient error with message=" + e.getMessage());
        }
    }

    public void create(ConsumerMonitor consumerMonitor, String consumerName) throws IllegalStateException {
        Thread thread = new Thread(this);
        thread.start();
        String tcName = thread.getName();
        System.out.println(this.started);
        if (this.started) {
            consumerMonitor.putThread(tcName, thread);
            logger.trace("consumer=" + consumerName + ",threadName=" + tcName + "已经启动...");
        } else {
            logger.trace("consumer=" + consumerName + ",threadName=" + tcName + "启动失败...");
            thread.interrupt();
        }
    }
}

