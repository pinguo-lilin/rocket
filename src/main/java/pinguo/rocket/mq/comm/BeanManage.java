package pinguo.rocket.mq.comm;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

public class BeanManage {

	/**
	 * 自动注册bean
	 * 
	 * @param beanClass		bean类
	 * @param beanName		bean名称
	 * @param properties	需要注册的属性
	 */
	public static void addConsumerBeanToFactory(Class<?> beanClass, String beanName, Map<String, Object> properties){
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) ApplicationContextUtil.getApplicationContext();
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		 
		if(!beanFactory.containsBean(beanName)){
			BeanDefinitionBuilder beanDefinitionBuilder= BeanDefinitionBuilder.rootBeanDefinition(beanClass);
			beanDefinitionBuilder.addConstructorArgValue(beanName);
			
			for(Map.Entry<String, Object> property:properties.entrySet()){    
				beanDefinitionBuilder.addPropertyValue(property.getKey(), property.getValue());
			}
			beanDefinitionBuilder.setScope("singleton");
			beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
		}
	}
	
	public static void refreshBeans(List<String> pinguoConsumers){
		//关闭producer连接
		DefaultMQProducer producer = (DefaultMQProducer) ApplicationContextUtil.getBean("PinGuoProducer");
		producer.shutdown();
		
		//关闭consumers连接
		for (String consumerName : pinguoConsumers) {
			Boolean isCreate = ApplicationContextUtil.contain(consumerName);
			if(isCreate == false){
				System.out.println("refresh consumer bean未创建， name=" + consumerName);
				continue;
			}
			
			DefaultMQPushConsumer pushConsumer = (DefaultMQPushConsumer) ApplicationContextUtil
					.getBean(consumerName);
			pushConsumer.shutdown();
		}
		
		//刷新bean
		WebApplicationContext  context = (WebApplicationContext) ApplicationContextUtil.getApplicationContext();  
		((AbstractRefreshableApplicationContext) context).refresh();
	}
}
