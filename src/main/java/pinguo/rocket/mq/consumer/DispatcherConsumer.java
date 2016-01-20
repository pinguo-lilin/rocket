package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;

import pinguo.rocket.mq.comm.ConsumerThread;
import pinguo.rocket.mq.comm.XmlHelper;
import pinguo.rocket.mq.consumer.listener.ThreadListener;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
    private final static Logger logger = LoggerFactory.getLogger(DispatcherConsumer.class);

    public static void main(String[] args) throws DocumentException {
        if (args.length == 0) {
            logger.error("args为空，没有可初始化的consumer");
            return;
        }
        String contextEnv = "testing";// 运行环境每次打包切换
        String consumerName = args[0];// 第一个参数默认consumerName
        String configPath = "rocket.xml";// rmq配置文件

        //解析xml
        XmlHelper xmlHelper = new XmlHelper(configPath);
        xmlHelper.parseXml();
        Map<String, Consumer> consumers = xmlHelper.getConsumers();
        Map<String, List<Subscribe>> subscribes = xmlHelper.getSubscribes();
        Map<String, Map<String, Map<String, Strategy>>> strategys = xmlHelper.getStrategys();

        //条件过滤
        if (!consumers.containsKey(consumerName) || !subscribes.containsKey(consumerName)) {
            logger.error("consumer没有配置，请确认配置文件rocket.xml");
            return;
        }
        Consumer consumer = consumers.get(consumerName);
        int threads = consumer.getThreads();
        //消费
        AbstractConsumer pushConsumer = new PushConsumer(consumerName);

        pushConsumer.setDefaultEnv(contextEnv);
        pushConsumer.setConsumers(consumers);
        pushConsumer.setSubscribes(subscribes);
        pushConsumer.setStrategys(strategys);

        ThreadListener threadListener = new ThreadListener();
        try {
            // 启动消费者多线程.
            for (int i = 0; i < threads; i ++) {
                ConsumerThread ct = new ConsumerThread(pushConsumer);

                Thread thread = new Thread(ct);
                thread.start();
                String tcName = thread.getName();
                threadListener.put(tcName, thread);
                logger.trace("consumer=" + consumerName + ",threadName=" + tcName + "已经启动...");
            }
        } catch (Exception e) {
            logger.error("consumer=" + consumerName + "启动失败，error=" + e.getMessage());
        }
    }
}
