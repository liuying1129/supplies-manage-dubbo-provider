package com.yklis.suppliesmanage.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 *  <!-- 配置扫描器,扫描value下的所有接口，然后创建各自接口的动态代理类 -->
 *  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"> 
 *      <property name="basePackage" value="com.yklis.lisfunction.dao" />
 *  </bean>
 * @author liuyi
 *
 */
@Configuration
public class DefMapperScannerConfig {

	//任何标志了@Bean的方法，其返回值将作为一个Bean注册到Spring的IOC容器中
	//方法名默认成为该bean定义的id
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
    	
    	MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    	mapperScannerConfigurer.setBasePackage("com.yklis.lisfunction.dao");
    	return mapperScannerConfigurer;
    }
}
