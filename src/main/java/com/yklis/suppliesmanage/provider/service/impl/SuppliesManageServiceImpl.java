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
		
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_RK_Fu");
	}
}
