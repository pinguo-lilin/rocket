package pinguo.rocket.mq.comm;

import com.alibaba.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pinguo.rocket.mq.consumer.AbstractConsumer;
import pinguo.rocket.mq.consumer.listener.ThreadListener;
import pinguo.rocket.mq.init.Rocketmq;

import java.util.Observable;

/**
 * ConsumerThread
 */
public class ConsumerThread extends Observable implements Runnable {

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

    public void create(ThreadListener threadListener, String consumerName) {
        Thread thread = new Thread(this);
        thread.start();
        String tcName = thread.getName();
        threadListener.put(tcName, thread);

        logger.trace("consumer=" + consumerName + ",threadName=" + tcName + "已经启动...");
    }
}

