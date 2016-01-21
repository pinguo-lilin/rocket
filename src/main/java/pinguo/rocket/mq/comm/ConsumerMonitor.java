package pinguo.rocket.mq.comm;

import java.util.Iterator;
import java.util.Map;

import java.util.concurrent.TimeUnit;

/**
 * 消费者线程监控器
 */
public class ConsumerMonitor implements Runnable {

    private Map<String, Thread> monitoredThreads;

    public ConsumerMonitor(Map<String, Thread> monitoredThreads) {
        this.monitoredThreads = monitoredThreads;
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
        Iterator iter = monitoredThreads.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Thread.State state = monitoredThreads.get(key).getState();
            if (state.TERMINATED.equals(state)) {
                // 终止线程
                monitoredThreads.get(key).interrupt();
                // 重新启动线程
                monitoredThreads.get(key).start();

            }
        }
    }
}
