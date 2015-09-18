package pinguo.rocket.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private final static Logger logger = LoggerFactory.getLogger(Test.class); 
	public static void main(String[] args) {
		logger.trace("file-trace2");
		logger.info("info message2");
		logger.error("error message2");
		Test t = new Test();
		t.testA();
	}
	public void testA(){
		logger.error("test a2");
	}
}
