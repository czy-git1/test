//package com.mall.controller;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ServerUploadUtil {
//
//    @Value("${upload.server.uuid}")
//    private String uuid;
//
//    @Value("${server.port:8030}")
//    private int serverPort;
//
//    @Value("${server.servlet.context-path:/mall-server}")
//    private String contextPath;
//
//    public String uploadToServer(String fileName, File file) {
//        // 安全构建URL：去掉所有空格
//        String port = String.valueOf(serverPort).trim();
//        String path = contextPath.trim() + "/upload";
//        path = path.replaceAll("//", "/");  // 防止双斜杠
//
//        String url = "http://192.168.43.31:" + port + path;
//        url = url.replaceAll("\\s+", "");  // 确保没有空格
//
//        System.out.println("上传URL: " + url);
//
//        String result = "";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        try {
//            HttpPost httpPost = new HttpPost(url);
//
//            // HttpMultipartMode.RFC6532参数的设定是为避免文件名为中文时乱码
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
//                    .setMode(HttpMultipartMode.RFC6532);
//
//            httpPost.addHeader("header1", "111"); // 头部放文件上传的head可自定义
//
//            // 重要：参数名改为 "file" 以匹配接收端
//            builder.addBinaryBody("myFile", file, ContentType.MULTIPART_FORM_DATA, fileName);
//            builder.addTextBody("fileName", file.getName()); // 其余参数，可自定义
//            builder.addTextBody("u", uuid);
//
//            HttpEntity entity = builder.build();
//            httpPost.setEntity(entity);
//
//            System.out.println("开始向图片服务器上传图片……");
//            HttpResponse response = httpClient.execute(httpPost); // 执行提交
//
//            int statusCode = response.getStatusLine().getStatusCode();
//            System.out.println("响应状态码: " + statusCode);
//
//            // 读取响应内容
//            result = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//            if (statusCode == HttpStatus.SC_OK) {
//                if (!StringUtils.isBlank(result)) {
//                    System.out.println("上传文件" + fileName + "返回参数==>" + result);
//                } else {
//                    System.out.println("上传文件失败：返回result为null");
//                    return null;
//                }
//            } else {
//                System.out.println("上传失败，HTTP状态码: " + statusCode + "，响应内容: " + result);
//                return null;
//            }
//
//        } catch (Exception e) {
//            System.out.println("上传文件异常：");
//            e.printStackTrace();
//            return null;
//        } finally { // 处理结束后关闭httpclient的链接
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 解析JSON前确保result不为空
//        if (StringUtils.isBlank(result)) {
//            System.out.println("响应内容为空，无法解析JSON");
//            return null;
//        }
//
//        try {
//            JSONObject json = JSON.parseObject(result);
//            if (json == null) {
//                System.out.println("JSON解析结果为null");
//                return null;
//            }
//
//            // 根据实际返回结构获取URL
//            // 常见返回格式1: { "url": "http://..." }
//            // 常见返回格式2: { "data": { "url": "http://..." } }
//            // 常见返回格式3: { "code": 200, "data": "http://..." }
//
//            String fileUrl = json.getString("url");
//
//            // 如果直接获取不到，尝试从data中获取
//            if (fileUrl == null && json.containsKey("data")) {
//                Object data = json.get("data");
//                if (data instanceof JSONObject) {
//                    fileUrl = ((JSONObject) data).getString("url");
//                } else if (data instanceof String) {
//                    fileUrl = (String) data;
//                }
//            }
//
//            if (fileUrl == null) {
//                System.out.println("无法从响应中获取URL，响应内容: " + result);
//                return null;
//            }
//
//            System.out.println("文件上传成功，URL: " + fileUrl);
//            return fileUrl;
//
//        } catch (Exception e) {
//            System.out.println("JSON解析失败: " + result);
//            e.printStackTrace();
//            return null;
//        }
//    }
//}