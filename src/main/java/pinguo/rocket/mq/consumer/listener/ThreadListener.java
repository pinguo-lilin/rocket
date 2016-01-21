package pinguo.rocket.mq.consumer.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 消费者线程监听控制器
 */
public class ThreadListener implements Observer{
    private Map<String, Thread> threads = new HashMap<String, Thread>();

    public boolean put(String threadName, Thread thread) {
        threads.put(threadName, thread);
        return true;
    }

    public boolean remove(String threadName) {
        if (threads.containsKey(threadName)) {
            threads.remove(threadName);
        }

        return true;
    }

    public Thread get(String threadName) {
        if (threads.containsKey(threadName)) {
            return threads.get(threadName);
        }

        return null;
    }

    public Map getAll() {
        return this.threads;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
