package pinguo.rocket.mq.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import pinguo.rocket.mq.entity.Msg;

/**
 * 测试逻辑处理
 */
@Service
public class TestService {
	public List<Msg> getData()
	{
		List<Msg> msgs = new ArrayList<Msg>();
		Msg msg1 = new Msg("appName", "opcode", "info", 0, "key");
		Msg msg2 = new Msg("appName2", "opcode2", "info2", 0, "key2");
		msgs.add(msg1);
		msgs.add(msg2);
		return msgs;
	}
}
