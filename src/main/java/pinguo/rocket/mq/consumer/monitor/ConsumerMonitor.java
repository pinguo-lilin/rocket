package pinguo.rocket.mq.consumer.monitor;

import pinguo.rocket.mq.consumer.ConsumerThread;
import pinguo.rocket.mq.consumer.AbstractConsumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.concurrent.TimeUnit;

/**
 * 消费者线程监控器
 */
public class ConsumerMonitor implements Runnable {

    private AbstractConsumer pushConsumer;
    private String consumerName;
    private Map<String, Thread> threads = new HashMap<String, Thread>();

    public ConsumerMonitor(String consumerName, AbstractConsumer pushConsumer) {
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
        Iterator iter = this.threads.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Thread thread = this.getThread(key);
            Thread.State state = thread.getState();
            if (state.TERMINATED.equals(state)) {
                // 终止线程
                thread.interrupt();
                this.removeThread(key);
                // 启动一个新线程
                ConsumerThread ct = new ConsumerThread(this.pushConsumer);
                ct.create(this, this.consumerName);
            }
        }
    }

    public boolean putThread(String threadName, Thread thread) {
        threads.put(threadName, thread);
        return true;
    }

    public boolean removeThread(String threadName) {
        if (threads.containsKey(threadName)) {
            threads.remove(threadName);
        }

        return true;
    }

    public Thread getThread(String threadName) {
        if (threads.containsKey(threadName)) {
            return threads.get(threadName);
        }

        return null;
    }

    public Map getThreads() {
        return this.threads;
    }
}
