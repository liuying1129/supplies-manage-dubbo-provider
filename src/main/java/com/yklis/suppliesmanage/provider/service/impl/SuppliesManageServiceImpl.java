package com.yklis.suppliesmanage.provider.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yklis.lisfunction.service.ExecSQLCmdService;
import com.yklis.lisfunction.service.ScalarSQLCmdService;
import com.yklis.lisfunction.service.SelectDataSetSQLCmdService;
import com.yklis.lisfunction.service.WorkerService;
import com.yklis.suppliesmanage.entity.ReceiptEntity;
import com.yklis.suppliesmanage.inf.SuppliesManageService;
import com.yklis.suppliesmanage.provider.util.Constants;
import com.yklis.suppliesmanage.provider.util.UnitsConverter;
import com.yklis.lisfunction.entity.WorkerEntity;
import com.yklis.util.CommFunction;

public class SuppliesManageServiceImpl implements SuppliesManageService {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SelectDataSetSQLCmdService selectDataSetSQLCmdService;
    
    @Autowired
    private ExecSQLCmdService execSQLCmdService;
    
    @Autowired
    private ScalarSQLCmdService scalarSQLCmdService;
    
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UnitsConverter unitsConverter;
	
    @Autowired
    private WorkerService workerService;

    @Override
	public String queryNoAuditReceiptList() {
		
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select Unid,SJUnid,SJID,Name,Model,GG,SCCJ,ApprovalNo,PH,CONVERT(CHAR(10),YXQ,121) as YXQ,SL,DW,SHR,CONVERT(CHAR(10),RKRQ,121) as RKRQ,Create_Date_Time,Vendor,DJH,Memo from SJ_RK_Fu where Audit_Date is null order by Unid desc");
	}
    
    @Override
    public String deleteReceipt(String unid) {
    	
    	return execSQLCmdService.ExecSQLCmd("delete from SJ_RK_Fu where unid="+unid+" and Audit_Date is null");
    }
    
    @Override
    public String insertReceipt(ReceiptEntity receiptEntity) {
    	
    	String sqlYxq;
    	if(null == receiptEntity.getYxq()) {sqlYxq = "null";} else {sqlYxq = "'"+receiptEntity.getYxq()+"'";}
    	
    	//logger.info("插入SQL:"+"insert into SJ_RK_Fu (SJUnid,Vendor,DJH,PH,YXQ,SL,DW,SHR,RKRQ,Memo) values ('"+receiptEntity.getSjunid()+"','"+receiptEntity.getVendor()+"','"+receiptEntity.getDjh()+"','"+receiptEntity.getPh()+"',"+sqlYxq+","+receiptEntity.getSl()+",'"+receiptEntity.getDw()+"','"+receiptEntity.getShr()+"','"+receiptEntity.getRkrq()+"','"+receiptEntity.getMemo()+"')");
    	return execSQLCmdService.ExecSQLCmd("insert into SJ_RK_Fu (SJUnid,Vendor,DJH,PH,YXQ,SL,DW,SHR,RKRQ,Memo) values ('"+receiptEntity.getSjunid()+"','"+receiptEntity.getVendor()+"','"+receiptEntity.getDjh()+"','"+receiptEntity.getPh()+"',"+sqlYxq+","+receiptEntity.getSl()+",'"+receiptEntity.getDw()+"','"+receiptEntity.getShr()+"','"+receiptEntity.getRkrq()+"','"+receiptEntity.getMemo()+"')");
    }
    
    @Override
    public String updateReceipt(ReceiptEntity receiptEntity) {
    	
    	String sqlYxq;
    	if(null == receiptEntity.getYxq()) {sqlYxq = "null";} else {sqlYxq = "'"+receiptEntity.getYxq()+"'";}

    	//logger.info("update SJ_RK_Fu set SJUnid="+receiptEntity.getSjunid()+",Vendor='"+receiptEntity.getVendor()+"',DJH='"+receiptEntity.getDjh()+"',PH='"+receiptEntity.getPh()+"',YXQ="+sqlYxq+",SL="+receiptEntity.getSl()+",DW='"+receiptEntity.getDw()+"',RKRQ='"+receiptEntity.getRkrq()+"' where Unid="+receiptEntity.getUnid());
    	return execSQLCmdService.ExecSQLCmd("update SJ_RK_Fu set SJUnid="+receiptEntity.getSjunid()+",Vendor='"+receiptEntity.getVendor()+"',DJH='"+receiptEntity.getDjh()+"',PH='"+receiptEntity.getPh()+"',YXQ="+sqlYxq+",SL="+receiptEntity.getSl()+",DW='"+receiptEntity.getDw()+"',RKRQ='"+receiptEntity.getRkrq()+"',Memo='"+receiptEntity.getMemo()+"' where Unid="+receiptEntity.getUnid()+" and Audit_Date is null");
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
    	return selectDataSetSQLCmdService.selectDataSetSQLCmd("select Unid,Name,GG,SCCJ,PH,CONVERT(CHAR(10),YXQ,121) as YXQ,SL,DW,SHR,Memo,CONVERT(CHAR(10),RKRQ,121) as RKRQ,Auditer,Audit_Date,Create_Date_Time,Vendor,DJH,Model,ApprovalNo from SJ_RK_Fu "+sqlRkrq+" order by Unid desc");
	}

	@Override
	public String audit(String unid,String userName) {
		
    	return execSQLCmdService.ExecSQLCmd("update SJ_RK_Fu set Auditer='"+userName+"',Audit_Date=getdate() where Unid="+unid+" and Audit_Date is null");
	}

	@Override
	public String queryInventoryList() {

		return selectDataSetSQLCmdService.selectDataSetSQLCmd("select Unid,SJUnid,RKID,Name,GG,SCCJ,PH,CONVERT(CHAR(10),YXQ,121) as YXQ,SL,DW,SHR,Memo,CONVERT(CHAR(10),RKRQ,121) as RKRQ,Create_Date_Time,GYS,Model,ApprovalNo from SJ_KC");
	}

	@Override
	public String outputInventory(String unid,String rlr,int sl,String dw,String ckrq,String memo,String shr) {
		
		List<Map<String, Object>> list;
    	try{
    		list = jdbcTemplate.queryForList("select * from SJ_KC where unid="+unid);
    	}catch(Exception e){
                
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "方法outputInventory,查询库存出错,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
    	}
        
        if(null==list||(list.size()!=1)) {
        	        	
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "没找到库存或找到多条库存记录,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
        }
                            
        int kcsSJUnid = (int) list.get(0).get("SJUnid");
        String kcsSJID = null==list.get(0).get("SJID")?"":list.get(0).get("SJID").toString();
        String kcsName = null==list.get(0).get("Name")?"":list.get(0).get("Name").toString();
        String kcsModel = null==list.get(0).get("Model")?"":list.get(0).get("Model").toString();
        String kcsGG = null==list.get(0).get("GG")?"":list.get(0).get("GG").toString();
        String kcsSCCJ = null==list.get(0).get("SCCJ")?"":list.get(0).get("SCCJ").toString();
        String kcsApprovalNo = null==list.get(0).get("ApprovalNo")?"":list.get(0).get("ApprovalNo").toString();
        String kcsPH = null==list.get(0).get("PH")?"":list.get(0).get("PH").toString();
        String kcsYXQ = null==list.get(0).get("YXQ")?"null":"'"+list.get(0).get("YXQ").toString()+"'";
        String kcsVendor = null==list.get(0).get("Vendor")?"":list.get(0).get("Vendor").toString();
        String kcsDW = null==list.get(0).get("DW")?"":list.get(0).get("DW").toString();
        int kcsSL = (int) list.get(0).get("SL");
        
        int dwRate = unitsConverter.UnitsConverterMethod(kcsSJUnid,kcsDW,dw);
        if(dwRate == 0) {
        	
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "没找到库存单位与出库单位的转换比例,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
        }
                
        if(
        ((dwRate>0)&&(kcsSL*dwRate<sl))||
        ((dwRate<0)&&(kcsSL<sl*Math.abs(dwRate)))
        ) {       	
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -123);
            mapResponse.put("errorMsg", "库存不足,出库失败!");
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);        
        }
        
        if(dwRate>0) {        	        	
        		
            try{
                jdbcTemplate.update("update SJ_KC set DW='"+dw+"',SL=SL*"+dwRate+"-"+ sl +" where unid="+unid);                            
            }catch(Exception e){
                    
                Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("errorCode", -223);
                mapResponse.put("errorMsg", "sql执行出错:"+e.toString());
                
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("response", mapResponse);
                
                return JSON.toJSONString(map);
            }        	
        }else {
        	
            try{
                jdbcTemplate.update("update SJ_KC set SL=SL-"+sl*Math.abs(dwRate)+" where unid="+unid);                            
            }catch(Exception e){
                    
                Map<String, Object> mapResponse = new HashMap<>();
                mapResponse.put("errorCode", -223);
                mapResponse.put("errorMsg", "sql执行出错:"+e.toString());
                
                Map<String, Object> map = new HashMap<>();
                map.put("success", false);
                map.put("response", mapResponse);
                
                return JSON.toJSONString(map);
            }        	
        }                
        		
        try{
            jdbcTemplate.update("insert into SJ_CK_Fu (KCUnid,SJUnid,SJID,Name,Model,GG,SCCJ,ApprovalNo,PH,YXQ,Vendor,RLR,CKRQ,SL,DW,Memo,SHR) values ("+unid+","+kcsSJUnid+",'"+kcsSJID+"','"+kcsName+"','"+kcsModel+"','"+kcsGG+"','"+kcsSCCJ+"','"+kcsApprovalNo+"','"+kcsPH+"',"+kcsYXQ+",'"+kcsVendor+"','"+rlr+"','"+ckrq+"',"+sl+",'"+dw+"','"+memo+"','"+shr+"')");
        }catch(Exception e){
                
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("errorCode", -223);
            mapResponse.put("errorMsg", "sql执行出错:"+e.toString());
            
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("response", mapResponse);
            
            return JSON.toJSONString(map);
        }		
		
        Map<String, Object> mapResponse = new HashMap<>();
        mapResponse.put("id", -1);
        mapResponse.put("msg", "出库成功");
        
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("response", mapResponse);
        
        return JSON.toJSONString(map);
	}

	@Override
	public String queryOutputList(String ckrqRadioValue) {
		
		String sqlCkrq;
		switch(ckrqRadioValue) {
		case "0":
			sqlCkrq = " where ckrq>GETDATE()-7 ";
			break;
		case "1":
			sqlCkrq = " where ckrq>GETDATE()-30 ";
			break;
		case "2":
			sqlCkrq = " where ckrq>GETDATE()-90 ";
			break;
		case "3":
			sqlCkrq = "";
			break;
		default:
			sqlCkrq = " where ckrq>GETDATE()-7 ";
			break;
		}

		return selectDataSetSQLCmdService.selectDataSetSQLCmd("select Unid,KCUnid,SJUnid,Name,Model,GG,SCCJ,ApprovalNo,PH,CONVERT(CHAR(10),YXQ,121) as YXQ,SL,DW,RLR,SHR,CONVERT(CHAR(10),CKRQ,121) as CKRQ,Create_Date_Time,Vendor,Memo from SJ_CK_Fu "+sqlCkrq+" order by Unid desc");
	}

	@Override
	public String querySqsydw() {
		
        //获取授权使用单位
        String s1 = scalarSQLCmdService.ScalarSQLCmd("select Name from CommCode where TypeName='系统代码' and ReMark='授权使用单位' ");
        //{"success":true,"response":{"result":""}}
                
        JSONObject jso=JSON.parseObject(s1);//json字符串转换成JSONObject(JSON对象)
        boolean bb1 = jso.getBooleanValue("success");
        
        String s2 = null;
        
        if(bb1){
            
            JSONObject jso2=jso.getJSONObject("response");
            String result =jso2.getString("result");
            
            s2 = CommFunction.deCryptStr(result, Constants.DES_KEY);            
        }
        
        return s2;
	}

	@Override
	public boolean login(String account,String password) {
		
        //account为null时Mybatis并不会作为空字符串""处理
        String tmpAccount = account;
        if(null == account){
            tmpAccount = "";
        }
        
        //passWord为null时Mybatis并不会作为空字符串""处理
    	String tmpPassword = password;
        if(null == password){
        	tmpPassword = "";
        }
        
        List<WorkerEntity> workerList = workerService.ifCanLogin(tmpAccount, tmpPassword);

        if((workerList == null)||(workerList.isEmpty())) return false;

		return true;
	}

	@Override
	public String queryUsernameFromUserid(String account) {
    	
		if(null==account)return null;
		
    	try{
    		//sql要求：
    		//1、有且仅有一条记录
    		//2、有且仅有一个字段
    		//3、字段在DB中的类型不限
    		String s = jdbcTemplate.queryForObject("select top 1 name from worker where id='"+account+"'",String.class);
	        
	    	return s;

    	}catch(Exception e){
                            
	    	return null;
    	}
	}
}
