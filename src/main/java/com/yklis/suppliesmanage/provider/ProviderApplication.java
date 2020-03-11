package com.yklis.suppliesmanage.provider;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * springboot运行dubbo优化启动器
 * https://jingyan.baidu.com/article/148a1921f906024d70c3b142.html
 * @author liuyi
 *
 * 右键启动
 */
@SpringBootApplication
//@DubboComponentScan(basePackages = "com.liu.boot.provider.provider.service")//注解配置方式
@ImportResource(value = {"classpath:dubbo-provider.xml"})
//<context:component-scan base-package="com.yklis.lisfunction.dao" />
@ComponentScan(basePackages="com.yklis.lisfunction.dao")
//<context:component-scan base-package="com.yklis.lisfunction.service.impl" />
@ComponentScan(basePackages="com.yklis.lisfunction.service.impl")
public class ProviderApplication {

	@Bean
	public CountDownLatch closeLatch() {
		
		return new CountDownLatch(1);
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		//SpringApplication.run(ProviderApplication.class, args);
		ApplicationContext context = new SpringApplicationBuilder()
				.sources(ProviderApplication.class)
		        .web(WebApplicationType.NONE)//禁用Web服务
		        .run(args);
		
		CountDownLatch closeLatch = context.getBean(CountDownLatch.class);
		//否则,springboot程序启动后会自动关闭
		closeLatch.await();//保持主线程阻塞;等待所有子线程完成
	}
}
