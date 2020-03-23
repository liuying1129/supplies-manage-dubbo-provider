package com.yklis.suppliesmanage.provider.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UnitsConverter {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param paramKcsdw 库存单位
	 * @param paramCksdw 出库单位
	 * @return 0:没找到转换关系,正数:转换比例,负数:转换比例
	 */
	public int UnitsConverterMethod(int paramKcsSjunid,String paramKcsdw,String paramCksdw) {
		
		String tmpParamKcsdw = null==paramKcsdw?"":paramKcsdw;
		String tmpParamCksdw = null==paramCksdw?"":paramCksdw;
		//库存单位、出库单位相同(包含两个单位均为空),则转换比例为1
		if(tmpParamKcsdw.equals(tmpParamCksdw)) return 1;
		
        if((null==paramKcsdw)||("".equals(paramKcsdw))||(null==paramCksdw)||("".equals(paramCksdw))) return 0;

    	//思路:通过库存单位查找出库单位,获得换算关系
    	//向下查找
        int iiDown = 1;
    	String s2 = paramKcsdw;
    	while (!paramCksdw.equals(s2)) {
			
    		List<Map<String, Object>> list;
        	try{
        		list = jdbcTemplate.queryForList("select * from SJ_Pack where SJUnid="+paramKcsSjunid+" and PackName='"+s2+"'");
        	}catch(Exception e){
                    
                logger.error("方法UnitsConverterMethod向下查找包装单位出错:"+e.toString());
                return 0;
        	}
        	
        	if((null==list)||(list.size()<=0)) break;
        	
        	s2 = list.get(0).get("SonPackName").toString();
        	if((null==s2)||("".equals(s2))) break;
        	int ii1 = (int) list.get(0).get("ParentSL");
        	iiDown = iiDown*ii1;
		}
    	
    	if(paramCksdw.equals(s2)) return iiDown;//找到
    	
    	//向上查找
    	int iiUp = -1;
    	String s3 = paramKcsdw;
    	while (!paramCksdw.equals(s3)) {
			
    		List<Map<String, Object>> list;
        	try{
        		list = jdbcTemplate.queryForList("select * from SJ_Pack where SJUnid="+paramKcsSjunid+" and SonPackName='"+s3+"'");
        	}catch(Exception e){
                    
                logger.error("方法UnitsConverterMethod向下查找包装单位出错:"+e.toString());
                return 0;
        	}
        	
        	if((null==list)||(list.size()<=0)) break;
        	
        	s3 = list.get(0).get("PackName").toString();
        	int ii1 = (int) list.get(0).get("ParentSL");
        	iiUp = iiUp*ii1;
		}
    	
    	if(paramCksdw.equals(s3)) return iiUp;//找到
    	
		return 0;
	}
}