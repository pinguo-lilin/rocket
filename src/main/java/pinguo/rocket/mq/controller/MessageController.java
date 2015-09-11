package pinguo.rocket.mq.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "msg")
public class MessageController {

	@RequestMapping(value = "send")
	@ResponseBody
	public String send(HttpServletRequest request, HttpServletResponse response){
		
		return "true1";
	}
}
