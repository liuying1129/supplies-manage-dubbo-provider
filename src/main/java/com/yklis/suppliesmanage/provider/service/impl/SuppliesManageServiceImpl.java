package com.yklis.suppliesmanage.provider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.yklis.lisfunction.service.SelectDataSetSQLCmdService;
import com.yklis.suppliesmanage.inf.SuppliesManageService;

public class SuppliesManageServiceImpl implements SuppliesManageService {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SelectDataSetSQLCmdService selectDataSetSQLCmdService;    

    @Override
	public String queryNoAuditReceiptList() {
		
        //Map<String, Object> map = new HashMap<>();
        //map.put("success", true);
        //map.put("response", list);

    	//return JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");

		String ss = selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_RK_Fu");
		//"{\"success\":true,\"response\":[{\"Unid\":123,\"Name\":\"KX21清洗液\"},{\"Unid\":124,\"Name\":\"尿试纸\"}]}";
		
		logger.info(ss);
		return ss;
	}

}
