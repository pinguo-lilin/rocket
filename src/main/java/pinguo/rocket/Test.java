package pinguo.rocket;
public class Test {

	public static void main(String[] args) {
		Producer producer = new Producer();
		producer.setAppName("");
		producer.setInfo("");
		producer.setKey("");
		producer.setOpCode("");
		producer.setTime(0);
		
		producer.send();
	}
}
