/**
 * 
 */
package TOI.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author zehengliu
 * 
 */
@Controller
public class UserController {

	@RequestMapping("/user/login.do")
	public ModelAndView testLogin2(String username, String password, ModelMap model) {
		model.put("username", username);
		model.put("password", password);
		if (!"admin".equals(username) || !"admin".equals(password)) {
			model.put("msg", "请输入正确的用户+密码登陆系统！");
			return new ModelAndView("user/login", model); // 手动实例化ModelAndView完成跳转页面（转发），效果等同于上面的方法返回字符串
		}
		// 采用重定向方式跳转页面
		return new ModelAndView(new RedirectView("../index.jsp"));
	}

}
