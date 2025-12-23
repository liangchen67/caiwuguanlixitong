package com.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 财务管理系统 - Spring Boot 启动类
 * 
 * <p>这是整个应用程序的入口类，负责启动Spring Boot应用。</p>
 * 
 * <p>系统功能模块：</p>
 * <ul>
 *   <li>总账管理：会计科目、会计分录、账簿查询</li>
 *   <li>业务管理：客户、供应商、员工、采购订单、销售单、员工报销</li>
 *   <li>银行管理：银行流水、银行对账</li>
 *   <li>报表管理：资产负债表、利润表、现金流量表</li>
 *   <li>基础信息：企业信息管理</li>
 * </ul>
 * 
 * <p>技术栈：</p>
 * <ul>
 *   <li>后端：Spring Boot 2.x + Spring Data JPA + MySQL</li>
 *   <li>前端：Vue 3 + Element Plus</li>
 * </ul>
 * 
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@SpringBootApplication
public class FinanceManagementApplication {
    
    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(FinanceManagementApplication.class, args);
        System.out.println("==================================================");
        System.out.println("财务管理系统启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("==================================================");
    }
}










