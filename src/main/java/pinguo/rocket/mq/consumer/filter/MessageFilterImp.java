package pinguo.rocket.mq.consumer.filter;

import com.alibaba.rocketmq.common.filter.MessageFilter;
import com.alibaba.rocketmq.common.message.MessageExt;

public class MessageFilterImp implements MessageFilter {

    @Override
    public boolean match(MessageExt msg) {
        return false;
    }

}
