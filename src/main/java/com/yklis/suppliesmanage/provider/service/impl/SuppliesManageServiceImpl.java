package com.yklis.suppliesmanage.provider.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    	
    	return execSQLCmdService.ExecSQLCmd("delete from SJ_RK_Fu where unid="+unid+" and Audit_Date is null");
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
    	return execSQLCmdService.ExecSQLCmd("update SJ_RK_Fu set SJUnid="+receiptEntity.getSjunid()+",Vendor='"+receiptEntity.getVendor()+"',DJH='"+receiptEntity.getDjh()+"',PH='"+receiptEntity.getPh()+"',YXQ="+sqlYxq+",SL="+receiptEntity.getSl()+",DW='"+receiptEntity.getDw()+"',RKRQ='"+receiptEntity.getRkrq()+"' where Unid="+receiptEntity.getUnid()+" and Audit_Date is null");
    }
    
    @Override
    public String loadSJ_JBXX() {
    	
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_JBXX");
    }

	@Override
	public String loadSJ_Pack(String sjunid) {
		
		String s1 = "select PackName from SJ_Pack where SJUnid="+sjunid+
					" UNION "+
					"select SonPackName from SJ_Pack where ISNULL(SonPackName,'')<>'' AND SJUnid="+sjunid;
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd(s1);
	}

	@Override
	public String queryReceiptList(String rkrqRadioValue) {
		
		String sqlRkrq;
		switch(rkrqRadioValue) {
		case "0":
			sqlRkrq = " where rkrq>GETDATE()-7 ";
			break;
		case "1":
			sqlRkrq = " where rkrq>GETDATE()-30 ";
			break;
		case "2":
			sqlRkrq = " where rkrq>GETDATE()-90 ";
			break;
		case "3":
			sqlRkrq = "";
			break;
		default:
			sqlRkrq = " where rkrq>GETDATE()-7 ";
			break;
		}
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_RK_Fu "+sqlRkrq+" order by Unid desc");
	}

	@Override
	public String audit(String unid) {
		
    	return execSQLCmdService.ExecSQLCmd("update SJ_RK_Fu set Auditer='"+"Auditer"+"',Audit_Date=getdate() where Unid="+unid+" and Audit_Date is null");
	}

	@Override
	public String queryInventoryList() {

		return selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_KC");
	}

	@Override
	public String outputInventory(String unid,String rlr,int sl,String dw,String ckrq,String memo) {
		
		String ss11 = selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_KC where unid="+unid);
        JSONObject jso11=JSON.parseObject(ss11);//json字符串转换成JSONObject(JSON对象)
        
        boolean bb11 = jso11.getBooleanValue("success");
        if(!bb11) return ss11;
        
        JSONArray jsarr11=jso11.getJSONArray("response");//JSONObject取得response对应的JSONArray(JSON数组)
        if(jsarr11.size()!=1) {
        	
        	logger.error("找到多条库存记录,出库失败!");
        	
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "找到多条库存记录,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
        }
                    
        JSONObject jso111 = jsarr11.getJSONObject(0);
        
        int kcsSJUnid = jso111.getIntValue("SJUnid");
        String kcsSJID = null==jso111.get("SJID")?"":jso111.get("SJID").toString();
        String kcsName = null==jso111.get("Name")?"":jso111.get("Name").toString();
        String kcsModel = null==jso111.get("Model")?"":jso111.get("Model").toString();
        String kcsGG = null==jso111.get("GG")?"":jso111.get("GG").toString();
        String kcsSCCJ = null==jso111.get("SCCJ")?"":jso111.get("SCCJ").toString();
        String kcsApprovalNo = null==jso111.get("ApprovalNo")?"":jso111.get("ApprovalNo").toString();
        String kcsPH = null==jso111.get("PH")?"":jso111.get("PH").toString();
        String kcsYXQ = null==jso111.get("YXQ")?"null":"'"+jso111.get("YXQ").toString()+"'";
        String kcsVendor = null==jso111.get("Vendor")?"":jso111.get("Vendor").toString();
        String kcsDW = null==jso111.get("DW")?"":jso111.get("DW").toString();
        int kcsSL = jso111.getIntValue("SL");
        
        if(kcsDW.equals(dw)) {//出库单位与库存单位一致
        	
        	if(kcsSL<sl) {
        		
                Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("errorCode", -123);
                mapResponse.put("errorMsg", "库存不够,出库失败!");
                
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("response", mapResponse);
                
                return JSON.toJSONString(map);
        	}
        	
    		execSQLCmdService.ExecSQLCmd("update SJ_KC set SL=SL-"+sl+" where unid="+unid);
        }else {
        	
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "出库单位与库存单位不同,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
        }
        		
		
		execSQLCmdService.ExecSQLCmd("insert into SJ_CK_Fu (KCUnid,SJUnid,SJID,Name,Model,GG,SCCJ,ApprovalNo,PH,YXQ,Vendor,RLR,CKRQ,SL,DW,Memo) values ("+unid+","+kcsSJUnid+",'"+kcsSJID+"','"+kcsName+"','"+kcsModel+"','"+kcsGG+"','"+kcsSCCJ+"','"+kcsApprovalNo+"','"+kcsPH+"',"+kcsYXQ+",'"+kcsVendor+"','"+rlr+"','"+ckrq+"',"+sl+",'"+dw+"','"+memo+"')");
		
		
        Map<String, Object> mapResponse = new HashMap<>();
        mapResponse.put("id", -1);
        mapResponse.put("msg", "出库成功");
        
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("response", mapResponse);
        
        return JSON.toJSONString(map);
	}

	@Override
	public String queryOutputList() {
		
		return selectDataSetSQLCmdService.selectDataSetSQLCmd("select * from SJ_CK_Fu");
	}
}
