package com.yklis.suppliesmanage.provider.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yklis.lisfunction.service.ExecSQLCmdService;
import com.yklis.lisfunction.service.SelectDataSetSQLCmdService;
import com.yklis.suppliesmanage.entity.ReceiptEntity;
import com.yklis.suppliesmanage.inf.SuppliesManageService;

public class SuppliesManageServiceImpl implements SuppliesManageService {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SelectDataSetSQLCmdService selectDataSetSQLCmdService;
    
    @Autowired
    private ExecSQLCmdService execSQLCmdService;

    @Override
	public String queryNoAuditReceiptList() {
		
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select Unid,SJUnid,SJID,Name,Model,GG,SCCJ,ApprovalNo,PH,CONVERT(CHAR(10),YXQ,121) as YXQ,SL,DW,SHR,CONVERT(CHAR(10),RKRQ,121) as RKRQ,Create_Date_Time,Vendor,DJH from SJ_RK_Fu where Audit_Date is null order by Unid desc");
	}
    
    @Override
    public String deleteReceipt(String unid) {
    	
    	return execSQLCmdService.ExecSQLCmd("delete from SJ_RK_Fu where unid="+unid);
    }
    
    @Override
    public String insertReceipt(ReceiptEntity receiptEntity) {
    	
    	String sqlYxq;
    	if(null == receiptEntity.getYxq()) {sqlYxq = "null";} else {sqlYxq = "'"+receiptEntity.getYxq()+"'";}
    	
    	//logger.info("insert into SJ_RK_Fu (SJUnid,Vendor,DJH,PH,YXQ,SL,DW,RKRQ) values ('"+receiptEntity.getSjunid()+"','"+receiptEntity.getVendor()+"','"+receiptEntity.getDjh()+"','"+receiptEntity.getPh()+"',"+sqlYxq+","+receiptEntity.getSl()+",'"+receiptEntity.getDw()+"','"+receiptEntity.getRkrq()+"')");
    	return execSQLCmdService.ExecSQLCmd("insert into SJ_RK_Fu (SJUnid,Vendor,DJH,PH,YXQ,SL,DW,RKRQ) values ('"+receiptEntity.getSjunid()+"','"+receiptEntity.getVendor()+"','"+receiptEntity.getDjh()+"','"+receiptEntity.getPh()+"',"+sqlYxq+","+receiptEntity.getSl()+",'"+receiptEntity.getDw()+"','"+receiptEntity.getRkrq()+"')");
    }
    
    @Override
    public String updateReceipt(ReceiptEntity receiptEntity) {
    	
    	String sqlYxq;
    	if(null == receiptEntity.getYxq()) {sqlYxq = "null";} else {sqlYxq = "'"+receiptEntity.getYxq()+"'";}

    	//logger.info("update SJ_RK_Fu set SJUnid="+receiptEntity.getSjunid()+",Vendor='"+receiptEntity.getVendor()+"',DJH='"+receiptEntity.getDjh()+"',PH='"+receiptEntity.getPh()+"',YXQ="+sqlYxq+",SL="+receiptEntity.getSl()+",DW='"+receiptEntity.getDw()+"',RKRQ='"+receiptEntity.getRkrq()+"' where Unid="+receiptEntity.getUnid());
    	return execSQLCmdService.ExecSQLCmd("update SJ_RK_Fu set SJUnid="+receiptEntity.getSjunid()+",Vendor='"+receiptEntity.getVendor()+"',DJH='"+receiptEntity.getDjh()+"',PH='"+receiptEntity.getPh()+"',YXQ="+sqlYxq+",SL="+receiptEntity.getSl()+",DW='"+receiptEntity.getDw()+"',RKRQ='"+receiptEntity.getRkrq()+"' where Unid="+receiptEntity.getUnid());
    }
}
