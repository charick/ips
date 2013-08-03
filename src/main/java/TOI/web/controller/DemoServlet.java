/**
 * 
 */
package TOI.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.velocity.VelocityContext;

import TOI.util.VelocityUtil;

/**
 * @author zehengliu
 * 
 */
public class DemoServlet extends HttpServlet {
	// 第二步：覆盖doGet()方法
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 第三步：获取HTTP请求中的参数信息
		String name = request.getParameter("name");

		// 第四步：生成HTTP响应结果
		PrintWriter out;
		// set content type
		response.setContentType("text/html;charset=UTF-8");
		// write html page
		out = response.getWriter();
		VelocityContext context = new VelocityContext();
		context.put("name", name);
		context.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
		out.print(VelocityUtil.filterVM("demo.vm", context));
		out.close();
	}
}
