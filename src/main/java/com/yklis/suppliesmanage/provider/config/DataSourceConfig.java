package com.yklis.suppliesmanage.provider.config;

import java.beans.PropertyVetoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 *   <bean id="dataSource_master" class="com.mchange.v2.c3p0.ComboPooledDataSource">
 *       <property name="driverClass" value="${jdbc.driverClass}"></property>
 *       <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
 *       <property name="user" value="${jdbc.user}"></property>
 *       <property name="password" value="${jdbc.password}"></property>
 *       <property name="initialPoolSize" value="${jdbc.pool.initialPoolSize}"></property>
 *       <property name="maxIdleTime" value="${jdbc.pool.maxIdleTime}"></property>
 *       <property name="maxPoolSize" value="${jdbc.pool.maxPoolSize}"></property>
 *       <property name="minPoolSize" value="${jdbc.pool.minPoolSize}"></property>
 *       <property name="checkoutTimeout" value="${jdbc.pool.checkoutTimeout}"></property>
 *   </bean>
 * @author liuyi
 *
 */
@Configuration
//不影响默认配置文件的读取
@PropertySource(value = {"file:/supplies-manage-cfg/jdbc.properties"})
public class DataSourceConfig {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jdbc.driverClass}")
    private String driverClass;
    @Value("${jdbc.jdbcUrl}")
    private String jdbcUrl;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    
    @Value("${jdbc.pool.initialPoolSize}")
    private int initialPoolSize;
    @Value("${jdbc.pool.maxIdleTime}")
    private int maxIdleTime;
    @Value("${jdbc.pool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${jdbc.pool.minPoolSize}")
    private int minPoolSize;
    @Value("${jdbc.pool.checkoutTimeout}")
    private int checkoutTimeout;
	
	@Bean
    public ComboPooledDataSource dataSource(){
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass(driverClass);		
			dataSource.setJdbcUrl(jdbcUrl);		
			dataSource.setUser(user);		
			dataSource.setPassword(password);
		
					
			//连接池初始化时创建的连接数,default : 3，取值应在minPoolSize与maxPoolSize之间
			dataSource.setInitialPoolSize(initialPoolSize);
		
			//连接的最大空闲时间，如果超过这个时间，某个数据库连接还没有被使用，则会断开掉这个连接。如果为0，则永远不会断开连接,即回收此连接。default : 0 单位 s
			dataSource.setMaxIdleTime(maxIdleTime);
		
			//连接池保持的最小连接数,default : 10
			dataSource.setMinPoolSize(minPoolSize);
		
			//连接池中拥有的最大连接数，如果获得新连接时会使连接总数超过这个值则不会再获取新连接，而是等待其他连接释放，所以这个值有可能会设计地很大,default : 100
			dataSource.setMaxPoolSize(maxPoolSize);
				
		    //连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时将抛出 SQLException,如设为0则无限期等待。单位毫秒。Default: 0
			dataSource.setCheckoutTimeout(checkoutTimeout);

		} catch (PropertyVetoException e) {
			
			logger.error("dataSource失败:" + e.toString());
			return null;
		}
		
		return dataSource;		
	}
}
