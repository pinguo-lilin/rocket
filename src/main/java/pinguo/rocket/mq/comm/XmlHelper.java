package pinguo.rocket.mq.comm;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pinguo.rocket.mq.entity.Consumer;
import pinguo.rocket.mq.entity.Strategy;
import pinguo.rocket.mq.entity.Subscribe;

/**
 * rocket.xml解析辅助类
 */
public class XmlHelper {
    private String xmlPath;
    private SAXReader saxReader;
    private Map<String, Consumer> consumers = new HashMap<String, Consumer>();
    private Map<String, List<Subscribe>> subscribes = new HashMap<String, List<Subscribe>>();
    private Map<String, Map<String, Map<String, Strategy>>> strategys = new HashMap<String, Map<String, Map<String, Strategy>>>();

    //日志初始化
    private final static Logger logger = LoggerFactory.getLogger(XmlHelper.class);

    /**
     * 初始化
     *
     * @param String xmlPath	xml文件路径
     */
    public XmlHelper(String xmlPath) {
        this.xmlPath = xmlPath;
        this.saxReader = new SAXReader();
    }

    /**
     * 解析xml
     */
    public void parseXml() {
        try {
            // 解决jar路径问题
            InputStream inputStream = this.getClass().getResourceAsStream("/" + this.xmlPath);
            Document document = this.saxReader.read(inputStream);
            Element root = document.getRootElement();
            this.parseConsumers(root);
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("rocket.xml解析异常，error=" + e.getMessage());
        }
    }

    /**
     * 解析consumers
     *
     * @param Element root 根节点
     */
    private void parseConsumers(Element root) {
        for (Iterator<?> iter = root.elementIterator(); iter.hasNext(); ) {
            Element consumerNode = (Element) iter.next();
            @SuppressWarnings("unchecked")
            List<Attribute> attrs = consumerNode.attributes();
            Consumer consumer = new Consumer();
            consumer = this.parseAttributes(consumer, attrs);

            this.consumers.put(consumer.getName(), consumer);
            this.parseSubscribes(consumer.getName(), consumerNode);
        }
    }

    /**
     * 解析subscribes
     *
     * @param String  consumerName	消费者名称
     * @param Element consumerNode	consumer节点
     */
    private void parseSubscribes(String consumerName, Element consumerNode) {
        List<Subscribe> subscribesList = new ArrayList<Subscribe>();
        Map<String, Map<String, Strategy>> topicTagStrategyMap = new HashMap<String, Map<String, Strategy>>();

        for (Iterator<?> topic = consumerNode.elementIterator(); topic.hasNext(); ) {
            Element topicNode = (Element) topic.next();
            String topicName = topicNode.attributeValue("name");

            Map<String, Strategy> tagStrategyMap = new HashMap<String, Strategy>();
            StringBuffer tagsSb = new StringBuffer();
            for (Iterator<?> tag = topicNode.elementIterator(); tag.hasNext(); ) {
                Element tagNode = (Element) tag.next();
                String tagName = tagNode.attributeValue("name");
                tagsSb.append(tagName);
                if (tag.hasNext()) {
                    tagsSb.append("||");
                }
                Strategy strategy = this.parseStrategys(tagNode);
                tagStrategyMap.put(tagName, strategy);
            }
            String tags = tagsSb.toString();
            Subscribe subscribe = new Subscribe();
            subscribe.setTopic(topicName);
            subscribe.setTags(tags);

            subscribesList.add(subscribe);
            topicTagStrategyMap.put(topicName, tagStrategyMap);
        }
        this.subscribes.put(consumerName, subscribesList);
        this.strategys.put(consumerName, topicTagStrategyMap);
    }

    /**
     * 解析strategys
     *
     * @param Element tagNode tag节点
     * @return Strategy
     */
    private Strategy parseStrategys(Element tagNode) {
        Strategy strategy = new Strategy();
        for (Iterator<?> item = tagNode.elementIterator(); item.hasNext(); ) {
            Element strategyNode = (Element) item.next();
            String name = strategyNode.getName();
            String value = strategyNode.getStringValue();
            strategy = (Strategy) this.callSetterMethodByNameAndValue(strategy, name, value);
        }
        return strategy;
    }

    /**
     * 解析consumer属性值
     *
     * @param Consumer        consumer	consumer对象
     * @param List<Attribute> attrs		consumer属性值集合
     * @return Consumer
     */
    private Consumer parseAttributes(Consumer consumer, List<Attribute> attrs) {
        for (Attribute attr : attrs) {
            String name = attr.getName();
            String value = attr.getValue();
            consumer = (Consumer) this.callSetterMethodByNameAndValue(consumer, name, value);
        }
        return consumer;
    }

    /**
     * 根据属性名称和值调用对应settter方法
     *
     * @param Object object 被调用对象
     * @param String name	属性名称
     * @param String value	属性值
     * @return Object
     */
    private Object callSetterMethodByNameAndValue(Object object, String name, String value) {
        Class<? extends Object> clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(name);
            String setMethodName = "set" + UtilHelper.toUpperFirstCase(name);

            //字符串类型
            if (field.getGenericType().toString().equals("class java.lang.String")) {
                Method getMethod = clazz.getDeclaredMethod(setMethodName, String.class);
                getMethod.invoke(object, value);

                //integer类型
            } else if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                Method getMethod = clazz.getDeclaredMethod(setMethodName, Integer.class);
                getMethod.invoke(object, new Integer(value));

                //double类型
            } else if (field.getGenericType().toString().equals("class java.lang.Double")) {
                Method getMethod = clazz.getDeclaredMethod(setMethodName, Double.class);
                getMethod.invoke(object, Double.parseDouble(value));

                //boolean类型
            } else if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                Method getMethod = clazz.getDeclaredMethod(setMethodName, boolean.class);
                getMethod.invoke(object, Boolean.getBoolean(value));

                //int类型
            } else if (field.getGenericType().toString().equals("int")) {
                Method getMethod = clazz.getDeclaredMethod(setMethodName, int.class);
                getMethod.invoke(object, Integer.parseInt(value));
            }
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        } catch (SecurityException securityException) {
            securityException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return object;
    }

    /************************
     * getter方法
     *****************************/
    public Map<String, Consumer> getConsumers() {
        return consumers;
    }

    public Map<String, List<Subscribe>> getSubscribes() {
        return subscribes;
    }

    public Map<String, Map<String, Map<String, Strategy>>> getStrategys() {
        return strategys;
    }
}
