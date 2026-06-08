package com.mall.controller.admin;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mall.dao.mapper.AdminMapper;
import com.mall.dao.mapper.UserMapper;
import com.mall.dao.model.Admin;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.mall.author.JwtConfig;
import com.mall.controller.vo.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {
	
	@Autowired
	private UserMapper userMapper;
	@Resource
	private JwtConfig jwtConfig ;
	@Autowired
	private AdminMapper adminMapper;

	@RequestMapping("logout")
	public String logOut(HttpSession session) {
		session.removeAttribute("currAdmin");
		return "admin/login";
	}
	
	/**
	 * 管理员登录表单
	 * @return
	 */
//	@RequestMapping("login")
//	public JsonResult loginForm(Admin admin) {
//
//		Admin queryAdmin = new Admin();
//		queryAdmin.setRealName(admin.getRealName()); // 账号
//		queryAdmin.setPwd(admin.getPwd());   // 密码
//
//
//		JsonResult jsonResult = new JsonResult();
//		// 同时根据 账号+密码 查询
//		List<Admin> list = adminMapper.select(queryAdmin);
//		System.out.println("查询到的管理员列表：" + list);
//		if(list!=null&&list.size()>0) {
//			Admin admin3 = list.get(0);
//			admin3.setLastLogin(new Date());
//			adminMapper.updateByPrimaryKeySelective(admin3);
//			//使用ID来签发token
//			String token = jwtConfig.createToken("ADMIN:"+admin3.getAdminId());
//			JSONObject json = new JSONObject();
//			json.put("admin", admin3);
//			json.put("status", "OK");
//			if (!StringUtils.isEmpty(token)) {
//				json.put("token",token);
//			}
//			jsonResult.setData(json);
//			return  jsonResult;
//		}else {
//			return JsonResult.error("500", "手机号或密码错误");
//		}
//	}

	@RequestMapping("login")
	public JsonResult loginForm(@RequestBody Admin admin) {

		System.out.println("=====================================");
		System.out.println("============= 我进入登录方法了 =============");
		System.out.println("=====================================");

		// 打印看看你到底收到了什么！！！
		System.out.println("前端传过来的 mobile：" + admin.getMobile());
		System.out.println("前端传过来的 pwd：" + admin.getPwd()); // 这里一定是 null！！！

		// 重点：强制手动封装查询条件，不让框架自动忽略 null
		Admin queryAdmin = new Admin();
		queryAdmin.setMobile(admin.getMobile());
		queryAdmin.setPwd(admin.getPwd()); // 即使是 null 也带上，让查询返回空

		List<Admin> list = adminMapper.select(queryAdmin);

		if (list != null && list.size() > 0) {
			Admin realAdmin = list.get(0);
			realAdmin.setLastLogin(new Date());
			adminMapper.updateByPrimaryKeySelective(realAdmin);

			String token = jwtConfig.createToken("ADMIN:" + realAdmin.getAdminId());
			JSONObject json = new JSONObject();
			json.put("admin", realAdmin);
			json.put("status", "OK");
			json.put("token", token);

			JsonResult result = new JsonResult();
			result.setData(json);
			return result;
		} else {
			return JsonResult.error("500", "手机号或密码错误");
		}
	}

	/**
	 * 修改个人信息
	 * @param session
	 * @param admin
	 * @return
	 */
	@RequestMapping("updateProfile")
	public JsonResult updateProfile(HttpSession session, Admin admin) {
		JsonResult result = new JsonResult();
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
			admin = adminMapper.selectByPrimaryKey(admin.getAdminId());
			result.setData(admin);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.error("-1", "更新失败");
		}
		return result;
	}
	
	@InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //注册自定义的编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
