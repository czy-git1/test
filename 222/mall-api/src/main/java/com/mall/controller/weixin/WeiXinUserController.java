package com.mall.controller.weixin;

import com.mall.dao.mapper.ConfigMapper;
import com.mall.dao.mapper.UserMapper;
import com.mall.dao.model.Config;
import com.mall.dao.model.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.author.JwtConfig;
import com.mall.controller.vo.JsonResult;
import com.mall.utils.NameUtils;

@RestController
@RequestMapping("/api/user")
public class WeiXinUserController {

	Logger logger = LoggerFactory.getLogger(WeiXinUserController.class);

	@Resource
	private JwtConfig jwtConfig ;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ConfigMapper configMapper;
	@Value("${wx.appid}")
	private String appid;
	@Value("${wx.secret}")
	private String secret;

	@PostMapping("/login")
	public JsonResult login (@RequestParam("openid") String openId){
		JSONObject json = new JSONObject();
		User record = new User();
		record.setOpenId(openId);
		User user = userMapper.selectOne(record);
		if(user==null) {
			return JsonResult.error("403", "用户不存在");
		}
		//将openid存入token中
		String token = jwtConfig.createToken(openId);
		if (!StringUtils.isEmpty(token)) {
			json.put("token",token) ;
		}
		return JsonResult.success(json) ;
	}

	@GetMapping("/getopenid")
	public JsonResult getOpenId(@RequestParam("code") String code, HttpServletRequest request) {
		// 读取当前模式，如果是试用预览版，就不要请求微信接口
		Config runModel = new Config();
		runModel.setConfKey("run_model");
		Config config = configMapper.selectOne(runModel);
		if(config.getValue().equalsIgnoreCase("exp")){
			// 试用预览版
			return expressModel();
		}
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		url += "?appid=" + appid;//小程序的appid
		url += "&secret=" + secret;//appSecret
		url += "&js_code=" + code;
		url += "&grant_type=authorization_code";
		url += "&connect_redirect=1";
		String res = null;
		JsonResult result = new JsonResult();
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			// DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);    //GET方式
			CloseableHttpResponse response = null;
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
					.setConnectTimeout(30000)                    // 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(30000)             // socket读写超时时间(单位毫秒)
					.setSocketTimeout(30000)                    // 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
			httpget.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
			response = httpClient.execute(httpget);                   // 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				res = EntityUtils.toString(responseEntity);
				logger.info("WXMP_OPENID获取用户openid响应:"+res);
			}
			// 释放资源
			if (httpClient != null) {
				httpClient.close();
			}
			if (response != null) {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.error("500", "获取用openid发生异常");
		}
		JSONObject jo = JSON.parseObject(res);
		String openid = jo.getString("openid");
		logger.info("WXMP_OPENID获取用户openid:"+openid);
		//判断是否新用户，如果是新用户则自动注册插入数据库，否则登录
		User record = new User();
		record.setOpenId(openid);
		User user = userMapper.selectOne(record);
		if(user==null) {
			//插入新用户，分配随机昵称，用户可以自己在个人中心修改
			user = new User();
			user.setStatus(1);
			user.setOpenId(openid);
			user.setGender("保密");
			user.setNickName(NameUtils.getName());
			userMapper.insertSelective(user);
		}
		//将openid存入token中
		String token = jwtConfig.createToken(openid);
		if (!StringUtils.isEmpty(token)) {
			jo.put("token",token) ;
		}
		//重新查询一下，把ID拿出来
		user = userMapper.selectOne(record);
		jo.put("user", user);
		result.setData(jo);
		return result;
	}

	/**
	 * 试用版，写死用户
	 * @return
	 */
	private JsonResult expressModel(){
		JsonResult result = new JsonResult();
		JSONObject jo = new JSONObject();
		Config wxKeyConfig = new Config();
		wxKeyConfig.setConfKey("wx_user_openid");
		Config config = configMapper.selectOne(wxKeyConfig);
		String openid = config.getValue();
		logger.info("试用版：WXMP_OPENID获取用户openid:"+openid);
		User record = new User();
		record.setOpenId(openid);
		//将openid存入token中
		String token = jwtConfig.createToken(openid);
		if (!StringUtils.isEmpty(token)) {
			jo.put("token",token) ;
		}
		jo.put("remark", "预览版小程序用户");
		//重新查询一下，把ID拿出来
		User user = userMapper.selectOne(record);
		jo.put("user", user);
		result.setData(jo);
		return result;
	}

	@RequestMapping("updateUser")
	public JsonResult updateUser(@RequestBody JSONObject reqJson, HttpServletRequest request) {
		User user = JSONObject.parseObject(reqJson.toJSONString(), User.class);
		userMapper.updateByPrimaryKeySelective(user);
		User newUser = userMapper.selectByPrimaryKey(user.getUserId());
		JsonResult ret = new JsonResult();
		ret.setData(newUser);
		return ret;
	}

	/**
	 * 首次登录，激活用户
	 * @param reqJson
	 * @param request
	 * @return
	 */
	@RequestMapping("active")
	public JsonResult activeUser(@RequestBody JSONObject reqJson, HttpServletRequest request) {
		User user = userMapper.selectByPrimaryKey(reqJson.getInteger("userId"));
		user.setLocation(reqJson.getString("city"));
		userMapper.updateByPrimaryKeySelective(user);

		JsonResult ret = new JsonResult();
		ret.setData(user);
		return ret;
	}
}
