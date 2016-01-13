package pinguo.rocket.mq.exception;

@SuppressWarnings("serial")
public class ProducerException extends Exception {
    public ProducerException(String msg) {
        super(msg);
    }
}
