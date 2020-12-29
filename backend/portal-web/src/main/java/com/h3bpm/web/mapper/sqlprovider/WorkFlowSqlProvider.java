package com.h3bpm.web.mapper.sqlprovider;

import java.util.Map;

public class WorkFlowSqlProvider {
   
    
    public String getBizObjectInfoByIdAndWorkflowCode(Map<String, Object> para){
    	String id = para.get("id") == null ? "" : (String) para.get("id");
    	String workflowCode = para.get("workflowCode") == null ? "" : (String) para.get("workflowCode");
        
        String sql =
                "SELECT"+
                        "			 ObjectID id,"+
                        "			 title,"+
                        "			 '"+workflowCode+"' workflowCode,"+
                        "			 business_sys businessSys"+
                        "			FROM"+
                        "				i_"+workflowCode+
                        "				WHERE 1=1  "+
                        "				AND ObjectID='"+id+"'";
        return sql;
    }
    
    public String getBizObjectInfoByBizObjectIdAndWorkflowCodeWithOutSysType(Map<String, Object> para){
    	String id = para.get("id") == null ? "" : (String) para.get("id");
    	String workflowCode = para.get("workflowCode") == null ? "" : (String) para.get("workflowCode");
        
        String sql =
                "SELECT"+
                        "			 ObjectID id,"+
                        "			 title,"+
                        "			 '"+workflowCode+"' workflowCode"+
                        "			FROM"+
                        "				i_"+workflowCode+
                        "				WHERE 1=1  "+
                        "				AND ObjectID='"+id+"'";
        return sql;
    }
}
