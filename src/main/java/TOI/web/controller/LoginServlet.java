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
public class LoginServlet extends HttpServlet {
	// 第二步：覆盖doGet()方法
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String name = request.getParameter("name");
		
		PrintWriter out;
		response.setContentType("text/html;charset=UTF-8");
		out = response.getWriter();
		if (name == null) {
			VelocityContext context = new VelocityContext();
			out.print(VelocityUtil.filterVM("login.vm", context));
		} else {
			VelocityContext context = new VelocityContext();
			context.put("name", name);
			context.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
			out.print(VelocityUtil.filterVM("demo.vm", context));
		}

		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
