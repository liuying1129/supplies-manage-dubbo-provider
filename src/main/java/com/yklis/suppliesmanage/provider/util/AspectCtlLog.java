package com.yklis.suppliesmanage.provider.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 切面:用于插入到指定位置实现某项功能的类
 * 
 * 记录service实现类被请求的日志
 * @author ying07.liu
 *
 */

//通过@AspectJ将类标识为切面
@Aspect
@Component
public class AspectCtlLog {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
      
    //Spring支持的5种增强：
    //前置增强:表示在目标方法执行前实施增强
    //后置增强:表示在目标方法执行后实施增强
    //环绕增强:表示在目标方法执行前后实施增强.可替代前、后置增强
    //抛出异常增强:表示在目标方法抛出异常后实施增强
    //引介增强:表示在目标类中添加一些新的方法和属性
    
    //定义增强类型:Before,前置增强；切点：由切点表达式确定
    @Before("execution(* com.yklis.suppliesmanage.provider.service.impl.*.*(..))")  
    //增强的横切逻辑
    public void doBefore(JoinPoint joinPoint) {  
    	
    	Signature signature = joinPoint.getSignature();
        logger.info("调用类【"+signature.getDeclaringTypeName()+"】的方法【"+signature.getName()+"】参数【"+JSON.toJSONString(joinPoint.getArgs())+"】begin");                
    }  
  
    @After("execution(* com.yklis.suppliesmanage.provider.service.impl.*.*(..))")  
    public void doAfter(JoinPoint joinPoint) {  

    	Signature signature = joinPoint.getSignature();
        logger.info("调用类【"+signature.getDeclaringTypeName()+"】的方法【"+signature.getName()+"】参数【"+JSON.toJSONString(joinPoint.getArgs())+"】end");
    }  
  
    @Around("execution(* com.yklis.suppliesmanage.provider.service.impl.*.*(..))")  
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        
        //在目标方法执行前调用
                          
        //通过反射机制调用目标方法
        //obj就是被拦截方法的返回值
    	Object obj = pjp.proceed();
        logger.info("doAround返回结果:"+JSON.toJSONString(obj));
        
        //在目标方法执行后调用
          
        return obj;  
    }  
}