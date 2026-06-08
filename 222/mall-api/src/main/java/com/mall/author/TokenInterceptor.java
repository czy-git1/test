package com.mall.author;

import com.mall.dao.mapper.AdminMapper;
import com.mall.dao.mapper.UserMapper;
import com.mall.dao.model.Admin;
import com.mall.dao.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtConfig jwtConfig ;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws SignatureException {
        System.out.println("进入拦截器……");

        /** 地址过滤：登录、登出、注册、图片访问 */
        String uri = request.getRequestURI() ;
        System.out.println("请求URI: " + uri);

        // ✅ 放行图片访问（关键修改）
        if (uri.contains("/images/")) {
            System.out.println("放行图片访问: " + uri);
            return true;
        }

        // ✅ 放行上传接口
        if (uri.contains("/upload")) {
            System.out.println("放行上传接口: " + uri);
            return true;
        }

        // ✅ 放行原有的路径
        if (uri.contains("login") || uri.contains("/api/user") || uri.contains("/api/index")){
            System.out.println("放行登录/用户/首页请求");
            return true;
        }

        boolean authRequired = true;
        if (handler instanceof HandlerMethod) {
            //注！
            HandlerMethod method = (HandlerMethod) handler;
            //获取方法上的注解
            AuthRequired authAnnontation = AnnotationUtils.findAnnotation(method.getMethod(), AuthRequired.class);
            System.out.println("注解: " + authAnnontation);
            if(authAnnontation==null){
                System.out.println("免登录请求！");
                return true;
//                authRequired = false;
            }
        }

        /** Token 验证 */
        String token = request.getHeader(jwtConfig.getHeader());
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(StringUtils.isEmpty(token)&&authRequired){
            throw new SignatureException(jwtConfig.getHeader()+ "不能为空");
        }

        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);//取到openid
            if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
                if(authRequired) {
                    throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
                }else {
                    return true;
                }
            }
        }catch (Exception e){
            if(authRequired) {
                throw new SignatureException(jwtConfig.getHeader() + "失效，请重新登录。");
            }
        }
        String tokenStr = claims.getSubject().toString();
        if(tokenStr.indexOf("ADMIN")>-1){
            // 后台用户
            Integer adminId = Integer.parseInt(tokenStr.substring(6));
            Admin admin = adminMapper.selectByPrimaryKey(adminId);
            //将当前登录用户放到request里
            request.setAttribute("loginAdmin", admin);
        }else{
            // 小程序用户
            User record = new User();
            record.setOpenId(tokenStr);
            User user = userMapper.selectOne(record);
            //将当前登录用户放到request里
            request.setAttribute("loginUser", user);
        }

        return true;
    }
}

