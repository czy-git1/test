package com.mall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mall.author.TokenInterceptor;

@Configuration
@Component
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.local.path:D:/uploads/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /images/** 映射到本地的上传目录
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath);

        System.out.println("静态资源映射：/images/** -> " + uploadPath);
    }

	@Autowired
    private TokenInterceptor tokenInterceptor ;
	
//	@Bean
//	TokenInterceptor tokenInterceptor() {
//		return new TokenInterceptor();
//	}
	
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("配置拦截器！");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("**/getopenid");
    }
}
