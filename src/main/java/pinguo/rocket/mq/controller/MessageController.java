package pinguo.rocket.mq.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

import pinguo.rocket.mq.comm.ResultCode;
import pinguo.rocket.mq.entity.Msg;
import pinguo.rocket.mq.entity.ResultStatus;
import pinguo.rocket.mq.exception.ProducerException;
import pinguo.rocket.mq.service.MessageService;

@Controller
@RequestMapping(value = "msg")
public class MessageController {

    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "send")
    @ResponseBody
    public ResultStatus send(HttpServletRequest request, HttpServletResponse response) {
        String appName = request.getParameter("appName");
        String opcode = request.getParameter("opcode");
        String info = request.getParameter("info");
        String time = request.getParameter("time");
        String key = request.getParameter("key");

        //参数验证
        if (appName.isEmpty() || opcode.isEmpty() || info.isEmpty() || time.isEmpty()) {
            logger.error("参数验证错误，appName" + appName + " opcode=" + opcode + " info=" + info + " time=" + time + " key=" + key);
            return new ResultStatus(ResultCode.PARAMETER_ERROR, "参数错误", null);
        }

        //appName(topic)存在性验证

        //过期消息处理
        float ftime = Float.parseFloat(time);

        Msg msg = new Msg(appName, opcode, info, ftime, key);
        boolean result = false;
        try {
            result = messageService.send(msg);
        } catch (MQClientException mqClientException) {
            logger.error("MQClientException:消息发送失败，error=" + mqClientException.getMessage());
        } catch (RemotingException remotingException) {
            logger.error("RemotingException:消息发送失败，error=" + remotingException.getMessage());
        } catch (MQBrokerException mqBrokerException) {
            logger.error("MQBrokerException:消息发送失败，error=" + mqBrokerException.getMessage());
        } catch (InterruptedException interruptedException) {
            logger.error("InterruptedException:消息发送失败，error=" + interruptedException.getMessage());
        } catch (ProducerException producerException) {
            logger.error("ProducerException:消息发送失败，error=" + producerException.getMessage());
        }

        if (result == true) {
            return new ResultStatus(ResultCode.SUCCESS, "消息发送成功", null);
        }
        return new ResultStatus(ResultCode.SERVER_ERROR, "服务器异常", null);
    }

    @RequestMapping(value = "show")
    public String showPage(ModelAndView model) {
        logger.error("日志测试error");
        return "show_message";
    }
}
