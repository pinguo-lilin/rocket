package pinguo.rocket.mq.entity;

public class Consumer {

    private String name;
    private String type;
    private boolean order;
    private int threads; // 消费者启动线程数
    private int persistConsumerOffsetInterval = 1000 * 5;
    private int heartbeatBrokerInterval = 1000 * 30;
    private int pollNameServerInteval = 1000 * 30;
    private int consumeThreadMin = 20;
    private int consumeThreadMax = 64;
    private int consumeConcurrentlyMaxSpan = 2000;
    private int pullThresholdForQueue = 1000;
    private long pullInterval = 0;
    private int consumeMessageBatchMaxSize = 1;
    private int pullBatchSize = 32;


    public int getConsumeThreadMin() {
        return consumeThreadMin;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public int getConsumeThreadMax() {
        return consumeThreadMax;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public int getConsumeConcurrentlyMaxSpan() {
        return consumeConcurrentlyMaxSpan;
    }

    public void setConsumeConcurrentlyMaxSpan(int consumeConcurrentlyMaxSpan) {
        this.consumeConcurrentlyMaxSpan = consumeConcurrentlyMaxSpan;
    }

    public int getPullThresholdForQueue() {
        return pullThresholdForQueue;
    }

    public void setPullThresholdForQueue(int pullThresholdForQueue) {
        this.pullThresholdForQueue = pullThresholdForQueue;
    }

    public long getPullInterval() {
        return pullInterval;
    }

    public void setPullInterval(long pullInterval) {
        this.pullInterval = pullInterval;
    }

    public int getConsumeMessageBatchMaxSize() {
        return consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    public int getPullBatchSize() {
        return pullBatchSize;
    }

    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    public int getPollNameServerInteval() {
        return pollNameServerInteval;
    }

    public void setPollNameServerInteval(int pollNameServerInteval) {
        this.pollNameServerInteval = pollNameServerInteval;
    }

    public int getHeartbeatBrokerInterval() {
        return heartbeatBrokerInterval;
    }

    public void setHeartbeatBrokerInterval(int heartbeatBrokerInterval) {
        this.heartbeatBrokerInterval = heartbeatBrokerInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getType() {
        return type;
    }

    public int getThreads() {
        return threads;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPersistConsumerOffsetInterval() {
        return persistConsumerOffsetInterval;
    }

    public void setPersistConsumerOffsetInterval(int persistConsumerOffsetInterval) {
        this.persistConsumerOffsetInterval = persistConsumerOffsetInterval;
    }

    public boolean getOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }
}
