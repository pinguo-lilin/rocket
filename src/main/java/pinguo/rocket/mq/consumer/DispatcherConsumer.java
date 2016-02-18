package pinguo.rocket.mq.consumer;

import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.exception.MQClientException;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pinguo.rocket.mq.comm.XmlHelper;
import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

public class DispatcherConsumer {
    private final static Logger logger = LoggerFactory.getLogger(DispatcherConsumer.class);

    public static void main(String[] args) throws DocumentException {
        if (args.length < 1) {
            logger.trace("参数输入错误");
            return;
        }
        String contextEnv = "testing";  // 运行环境每次打包切换
        String consumerName = args[0];
        String configPath = "rocket.xml";// rmq配置文件

        // 解析xml
        XmlHelper xmlHelper = new XmlHelper(configPath);
        xmlHelper.parseXml();
        Map<String, Consumer> consumers = xmlHelper.getConsumers();
        Map<String, List<Subscribe>> subscribes = xmlHelper.getSubscribes();
        Map<String, Map<String, Map<String, Strategy>>> strategys = xmlHelper.getStrategys();

        //条件过滤
        if (!consumers.containsKey(consumerName) || !subscribes.containsKey(consumerName)) {
            logger.error("Consumer没有配置，请确认配置文件rocket.xml");
            return;
        }
        // 消费
        AbstractConsumer pushConsumer = new PushConsumer(consumerName);

        pushConsumer.setDefaultEnv(contextEnv);
        pushConsumer.setConsumers(consumers);
        pushConsumer.setSubscribes(subscribes);
        pushConsumer.setStrategys(strategys);

        try {
            pushConsumer.start();
        } catch (MQClientException e) {
            logger.error("Start MQClient failed with message=" + e.getMessage());
        }
    }
}
