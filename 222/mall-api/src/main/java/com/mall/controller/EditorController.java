package com.mall.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mall.controller.vo.WangEditor;

@Controller
public class EditorController {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${upload.local.path:D:/uploads/}")  // 配置文件可自定义存储路径
	private String uploadPath;

	@Value("${server.port:8030}")
	private int serverPort;

	@Value("${server.servlet.context-path:/mall-server}")
	private String contextPath;

	// 图片上传接口（供前端调用）
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public WangEditor uploadFile(
			@RequestParam("myFile") MultipartFile multipartFile,
			HttpServletRequest request) {

		System.out.println("========== 收到前端上传请求 ==========");
		System.out.println("原始文件名：" + multipartFile.getOriginalFilename());
		System.out.println("文件大小：" + multipartFile.getSize() + " 字节");
		System.out.println("文件类型：" + multipartFile.getContentType());

		FileOutputStream fos = null;
		try {
			// 1. 获取文件输入流
			InputStream inputStream = multipartFile.getInputStream();

			// 2. 生成唯一文件名
			String originalFilename = multipartFile.getOriginalFilename();
			String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString();
			String fileName = uuid + suffix;

			// 3. 确保上传目录存在
			String savePath = uploadPath;
			// 如果配置的是相对路径，则使用项目路径
			if (!savePath.startsWith("D:") && !savePath.startsWith("C:")) {
				savePath = request.getSession().getServletContext().getRealPath("/") + savePath;
			}

			File uploadDir = new File(savePath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
				System.out.println("创建上传目录：" + savePath);
			}

			// 4. 保存文件到实际存储位置
			String fullPath = savePath + fileName;
			File destFile = new File(fullPath);
			FileUtils.copyInputStreamToFile(inputStream, destFile);

			System.out.println("文件已保存到：" + fullPath);


			// 5. 生成可访问的图片URL（确保没有空格）
			String baseUrl = "http://localhost:" + serverPort;
			String cleanContextPath = contextPath.trim();  // 关键：去掉空格
			String urlPath = "/images/" + fileName;

			String picUrl = baseUrl + cleanContextPath + urlPath;
			picUrl = picUrl.replaceAll("\\s+", "");  // 确保没有任何空格

			System.out.println("图片访问URL：" + picUrl);
			System.out.println("URL长度：" + picUrl.length());

			// 在 EditorController.java 的 uploadFile 方法中
			// 6. 返回给前端
			String[] str = { picUrl };
			WangEditor we = new WangEditor(str);

			System.out.println("========== 上传成功 ==========");
			return we;

		} catch (Exception e) {
			log.error("上传文件失败", e);
			System.out.println("========== 上传失败 ==========");
			e.printStackTrace();
			return null;
		}
	}

	// 生成文件名的方法（保留你原来的方法）
	public String getFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStr = sdf.format(new Date());
		String str = RandomStringUtils.random(5,
				"abcdefghijklmnopqrstuvwxyz1234567890");
		return timeStr + str + ".jpg";
	}
}