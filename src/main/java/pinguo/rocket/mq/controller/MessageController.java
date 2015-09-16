package pinguo.rocket.mq.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pinguo.rocket.mq.comm.ResultCode;
import pinguo.rocket.mq.entity.Msg;
import pinguo.rocket.mq.entity.ResultStatus;
import pinguo.rocket.mq.service.MessageService;

@Controller
@RequestMapping(value = "msg")
public class MessageController {

	@Resource
	private MessageService messageService;
	
	@RequestMapping(value = "send")
	@ResponseBody
	public ResultStatus send(HttpServletRequest request, HttpServletResponse response){
		String appName = request.getParameter("appName");
		String opcode = request.getParameter("opcode");
		String info = request.getParameter("info");
		String time = request.getParameter("time");
		String key = request.getParameter("key");
		
		//参数验证
		if(appName.isEmpty() || opcode.isEmpty() || info.isEmpty() || time.isEmpty()){
			return new ResultStatus(ResultCode.PARAMETER_ERROR, "参数错误", null);
		}
		
		//appName(topic)存在性验证
		
		//过期消息处理
		float ftime = Float.parseFloat(time);
		
		Msg msg = new Msg(appName, opcode, info, ftime, key);
		
		return messageService.send(msg);
	}
	
	@RequestMapping(value = "show")
	public String showPage(ModelAndView model){
		return "show_message";
	}
}
