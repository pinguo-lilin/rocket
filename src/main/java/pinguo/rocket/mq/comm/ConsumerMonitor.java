package pinguo.rocket.mq.comm;

import pinguo.rocket.mq.consumer.AbstractConsumer;
import pinguo.rocket.mq.consumer.PushConsumer;
import pinguo.rocket.mq.consumer.listener.ThreadListener;

import java.util.Iterator;
import java.util.Map;

import java.util.concurrent.TimeUnit;

/**
 * 消费者线程监控器
 */
public class ConsumerMonitor implements Runnable {

    private ThreadListener threadListener;
    private AbstractConsumer pushConsumer;
    private String consumerName;

    public ConsumerMonitor(ThreadListener threadListener, AbstractConsumer pushConsumer, String consumerName) {
        this.threadListener = threadListener;
        this.pushConsumer = pushConsumer;
        this.consumerName = consumerName;
    }

    @Override
    public void run() {
        while (true) {
            monitor();
            try {
                TimeUnit.MICROSECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 监控逻辑
     */
    private void monitor() {
        Iterator iter = this.threadListener.getAll().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Thread.State state = this.threadListener.get(key).getState();
            if (state.TERMINATED.equals(state)) {
                // 终止线程
                this.threadListener.get(key).interrupt();
                this.threadListener.remove(key);
                // 重新启动线程
                ConsumerThread ct = new ConsumerThread(this.pushConsumer);
                ct.create(threadListener, this.consumerName);
            }
        }
    }
}
