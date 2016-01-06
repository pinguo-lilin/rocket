package pinguo.rocket.mq.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pinguo.rocket.mq.entity.Msg;
import pinguo.rocket.mq.entity.ResultStatus;
import pinguo.rocket.mq.service.TestService;

@Controller
@RequestMapping(value = "test")
public class TestController {

	@Resource
	private TestService testService;
	
	//显示普通页面
	@RequestMapping(value = "page", method = RequestMethod.GET)
	public String page() {
		return "page";
	}

	//方式1：接受参数
	@RequestMapping(value = "pars", method = RequestMethod.GET)
	public String params(HttpServletRequest request, HttpServletResponse response) {
		String one = request.getParameter("one");
		String two = request.getParameter("two");
		System.out.println(one + "_" + two);
		return "page";
	}

	//方式2：接受参数（路径参数）
	@RequestMapping(value = "path/{id}")
	public String paramsPath(@PathVariable int id) {
		System.out.println("id=" + id);
		return "page";
	}
	
	//返回json格式数据
	@RequestMapping(value = "json")
	@ResponseBody
	public ResultStatus json()
	{
		ResultStatus rs = new ResultStatus(200, "error", null);
		return rs;
	}
	
	//数据传递到页面
	@RequestMapping(value = "data")
	public String showData(Model model, HttpServletRequest request, HttpServletResponse response){
		List<Msg> msgs = new ArrayList<Msg>();
		Msg msg1 = new Msg("appName", "opcode", "info", 0, "key");
		Msg msg2 = new Msg("appName2", "opcode2", "info2", 0, "key2");
		msgs.add(msg1);
		msgs.add(msg2);
		
		for(Msg msg : msgs){
			System.out.println("appName:"+msg.getAppName());
			System.out.println("info2:"+msg.getInfo());
		}
		
		model.addAttribute("data", "modelData");
		model.addAttribute("msgs", msgs);
		return "test_data";
	}
	
	//对象注入
	@RequestMapping(value = "service")
	public String service(Model model, HttpServletRequest request, HttpServletResponse response){
		List<Msg> msgs = this.testService.getData();
		for(Msg msg : msgs){
			System.out.println("appName:"+msg.getAppName());
			System.out.println("info2:"+msg.getInfo());
		}
		
		model.addAttribute("data", "modelData");
		model.addAttribute("msgs", msgs);
		return "test_data";
	}
}
