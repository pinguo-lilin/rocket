package pinguo.rocket.mq.consumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pinguo.rocket.mq.comm.ConsumerMonitor;
import pinguo.rocket.mq.comm.ConsumerThread;
import pinguo.rocket.mq.comm.XmlHelper;
import pinguo.rocket.mq.consumer.listener.ThreadListener;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
    private final static Logger logger = LoggerFactory.getLogger(DispatcherConsumer.class);

    public static void main(String[] args) throws DocumentException {
        String contextEnv = "testing";  // 运行环境每次打包切换
        String name = "";
        if (args.length > 0 && ! args[0].isEmpty()) {
            name = args[0]; // 第一个参数默认consumerName
        }
        String configPath = "rocket.xml";// rmq配置文件

        // 解析xml
        XmlHelper xmlHelper = new XmlHelper(configPath);
        xmlHelper.parseXml();
        Map<String, Consumer> consumers = xmlHelper.getConsumers();
        Map<String, List<Subscribe>> subscribes = xmlHelper.getSubscribes();
        Map<String, Map<String, Map<String, Strategy>>> strategys = xmlHelper.getStrategys();

        Map<String, Consumer> startConsumers = new HashMap<>();
        // 输入consumer名称,启动指定consumer
        if (name.isEmpty()) {
            startConsumers = consumers;
        } else {
            //条件过滤
            if (!consumers.containsKey(name) || !subscribes.containsKey(name)) {
                logger.error("consumer没有配置，请确认配置文件rocket.xml");
                return;
            }
            startConsumers.put(name, consumers.get(name));
        }
        System.out.println(startConsumers);
        Iterator it = startConsumers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String consumerName = (String) entry.getKey();
            Consumer consumer = (Consumer) entry.getValue();
            int threads = consumer.getThreads();
            // 消费
            AbstractConsumer pushConsumer = new PushConsumer(consumerName);

            pushConsumer.setDefaultEnv(contextEnv);
            pushConsumer.setConsumers(consumers);
            pushConsumer.setSubscribes(subscribes);
            pushConsumer.setStrategys(strategys);
            // 一个消费者对应一个监听器
            ThreadListener threadListener = new ThreadListener();
            try {
                // 启动消费者多线程.
                for (int i = 0; i < threads; i++) {
                    ConsumerThread ct = new ConsumerThread(pushConsumer);
                    ct.create(threadListener, consumerName);
                }
            } catch (IllegalStateException e) {
                logger.error("consumer=" + consumerName + "启动失败，error=" + e.getMessage());
            }

            // 启动监控器
            ConsumerMonitor cMonitor = new ConsumerMonitor(consumerName, threadListener, pushConsumer);
            try {
                Thread tMonitor = new Thread(cMonitor);
                tMonitor.start();
            } catch (IllegalStateException e) {
                logger.trace("monitor 启动失败...");
                return;
            }
            logger.trace("monitor 已经启动...");
        }
    }
}
