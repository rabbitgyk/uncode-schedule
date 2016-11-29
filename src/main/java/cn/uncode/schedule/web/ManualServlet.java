package cn.uncode.schedule.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import cn.uncode.schedule.ConsoleManager;
import cn.uncode.schedule.core.TaskDefine;

@WebServlet(name="schedule",urlPatterns="/schedule/manual")
public class ManualServlet extends HttpServlet{

	private static final long serialVersionUID = -1293802185335109372L;
	
	private Gson gson = new Gson();

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Map<String, String> result = new HashMap<String, String>();
		String bean = request.getParameter("bean");
		String method = request.getParameter("method");
		if(StringUtils.isNotEmpty(bean) && StringUtils.isNotEmpty(method)){
			TaskDefine taskDefine = new TaskDefine();
			taskDefine.setTargetBean(bean);
			taskDefine.setTargetMethod(method);
			String param = request.getParameter("param");
			if(StringUtils.isNotEmpty(param)){
				taskDefine.setParams(param);
			}
			try {
				ConsoleManager.runTask(taskDefine);
				result.put("code", "0000");
				result.put("msg", "调用成功");
			} catch (Exception e) {
				result.put("code", "8888");
				result.put("msg", e.getMessage());
			}
		}
		
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(gson.toJson(result));
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
