package com.finance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 
 * <p>配置Spring MVC的相关设置，主要包括跨域资源共享（CORS）配置。</p>
 * 
 * <p>CORS配置说明：</p>
 * <ul>
 *   <li>允许所有源（origin）的跨域请求</li>
 *   <li>支持GET、POST、PUT、DELETE、OPTIONS方法</li>
 *   <li>允许所有请求头</li>
 *   <li>允许携带凭证（如Cookie）</li>
 *   <li>预检请求缓存时间：3600秒（1小时）</li>
 * </ul>
 * 
 * <p>注意：生产环境建议配置具体的允许源地址，而不是使用通配符。</p>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 添加跨域映射配置
     * 
     * <p>允许前端应用（通常运行在不同端口）访问后端API。</p>
     * 
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有路径应用CORS配置
                .allowedOriginPatterns("*")  // 允许所有源（开发环境）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true)  // 允许携带凭证
                .maxAge(3600);  // 预检请求缓存时间（秒）
    }
}










