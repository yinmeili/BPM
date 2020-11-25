package com.h3bpm.web.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.MonitorNode;
import com.h3bpm.web.entity.MonitorNodeHistory;

public interface LiquidationMonitorMapper {
	@Select("SELECT `ObjectID` from `h3bpm`.`i_liquidation` WHERE `node_load_status` = #{nodeLoadStatus}")
	public List<String> findMonitorNodeFlowIds(@Param("nodeLoadStatus") int nodeLoadStatus);

	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_moan WHERE `ParentObjectID` = #{flowId} AND node_name = #{nodeName}")
	public MonitorNode getMarketOpenAtNightMonitorNodeByFlowIdAndNodeName(@Param("flowId") String flowId, @Param("nodeName") String nodeName);
	
	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_moan WHERE `ParentObjectID` = #{flowId}")
	public List<MonitorNode> findMarketOpenAtNightMonitorNodeByFlowId(@Param("flowId") String flowId);

	@Insert({ "INSERT INTO `h3bpm`.`i_liquidation_monitor_moan` (`ObjectID`, `node_execute_result`, `node_desc`, `node_status`, `node_name`, `ParentIndex`, `ParentPropertyName`, `ParentObjectID`) VALUES (#{objectID}, #{executeResult}, #{desc}, #{status}, #{name}, #{parentIndex}, #{parentPropertyName}, #{parentObjectID})" })
	public void createMarketOpenAtNightMonitorNode(MonitorNode monitorNode);

	@Update({ "update `h3bpm`.`i_liquidation_monitor_moan` set `node_execute_result` = #{executeResult}, `node_desc` = #{desc}, `node_status` = #{status}, `node_name` = #{name}, `ParentIndex` = #{parentIndex}, `ParentPropertyName` = #{parentPropertyName}, `ParentObjectID` = #{parentObjectID} WHERE `ObjectID`=#{objectID}" })
	public void updateMarketOpenAtNightMonitorNode(MonitorNode monitorNode);
	
	
	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_stmarket WHERE `ParentObjectID` = #{flowId} AND node_name = #{nodeName}")
	public MonitorNode getStockToMarketMonitorNodeByFlowIdAndNodeName(@Param("flowId") String flowId, @Param("nodeName") String nodeName);
	
	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_stmarket WHERE `ParentObjectID` = #{flowId}")
	public List<MonitorNode> findStockToMarketMonitorNodeByFlowId(@Param("flowId") String flowId);

	@Insert({ "INSERT INTO `h3bpm`.`i_liquidation_monitor_stmarket` (`ObjectID`, `node_execute_result`, `node_desc`, `node_status`, `node_name`, `ParentIndex`, `ParentPropertyName`, `ParentObjectID`) VALUES (#{objectID}, #{executeResult}, #{desc}, #{status}, #{name}, #{parentIndex}, #{parentPropertyName}, #{parentObjectID})" })
	public void createStockToMarketMonitorNode(MonitorNode monitorNode);

	@Update({ "update `h3bpm`.`i_liquidation_monitor_stmarket` set `node_execute_result` = #{executeResult}, `node_desc` = #{desc}, `node_status` = #{status}, `node_name` = #{name}, `ParentIndex` = #{parentIndex}, `ParentPropertyName` = #{parentPropertyName}, `ParentObjectID` = #{parentObjectID} WHERE `ObjectID`=#{objectID}" })
	public void updateStockToMarketMonitorNode(MonitorNode monitorNode);
	
	
	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_deal WHERE `ParentObjectID` = #{flowId} AND node_name = #{nodeName}")
	public MonitorNode getDealMonitorNodeByFlowIdAndNodeName(@Param("flowId") String flowId, @Param("nodeName") String nodeName);
	
	@Select("SELECT `ObjectID` objectID, `node_execute_result` executeResult, `node_desc` `desc`, `node_status` `status`, `node_name` `name`, `ParentIndex` `parentIndex`, `ParentPropertyName` `parentPropertyName`, `ParentObjectID` `parentObjectID` from `h3bpm`.i_liquidation_monitor_deal WHERE `ParentObjectID` = #{flowId}")
	public List<MonitorNode> findDealMonitorNodeByFlowId(@Param("flowId") String flowId);

	@Insert({ "INSERT INTO `h3bpm`.`i_liquidation_monitor_deal` (`ObjectID`, `node_execute_result`, `node_desc`, `node_status`, `node_name`, `ParentIndex`, `ParentPropertyName`, `ParentObjectID`) VALUES (#{objectID}, #{executeResult}, #{desc}, #{status}, #{name}, #{parentIndex}, #{parentPropertyName}, #{parentObjectID})" })
	public void createDealMonitorNode(MonitorNode monitorNode);

	@Update({ "update `h3bpm`.`i_liquidation_monitor_deal` set `node_execute_result` = #{executeResult}, `node_desc` = #{desc}, `node_status` = #{status}, `node_name` = #{name}, `ParentIndex` = #{parentIndex}, `ParentPropertyName` = #{parentPropertyName}, `ParentObjectID` = #{parentObjectID} WHERE `ObjectID`=#{objectID}" })
	public void updateDealMonitorNode(MonitorNode monitorNode);
	
	
	
	
	@Select("SELECT `id` , `node_execute_result` executeResult, `node_status` `status`, `node_name` `name`, `monitor_type` monitorType, `monitor_date` `monitorDate` from `h3bpm`.i_liquidation_monitor_history WHERE `node_name` =#{nodeName} AND `monitor_type` = #{monitorType} AND `monitor_date` = #{monitorDate}")
	public MonitorNodeHistory getMonitorNodeHistoryByNodeNameAndTypeAndDate(@Param("nodeName") String nodeName,@Param("monitorType") String monitorType, @Param("monitorDate") Date monitorDate);
	
	@Select("SELECT `id` , `node_execute_result` executeResult, `node_status` `status`, `node_name` `name`, `monitor_type` monitorType, `monitor_date` `monitorDate` from `h3bpm`.i_liquidation_monitor_history WHERE `monitor_type` = #{monitorType} AND `monitor_date` = #{monitorDate}")
	public List<MonitorNodeHistory> findMonitorNodeHistoryByTypeAndDate(@Param("monitorType") String monitorType, @Param("monitorDate") Date monitorDate);

	@Insert({ "INSERT INTO `h3bpm`.`i_liquidation_monitor_history` (`id`, `node_execute_result`, `node_status`, `node_name`, `monitor_type` , `monitor_date`) VALUES (#{id}, #{executeResult}, #{status}, #{name}, #{monitorType}, #{monitorDate})" })
	public void createMonitorNodeHistory(MonitorNodeHistory monitorNodeHistory);

	@Update({ "update `h3bpm`.`i_liquidation_monitor_history` set `node_execute_result` = #{executeResult}, `node_status` = #{status}, `node_name` = #{name}, `monitor_type` = #{monitorType}, `monitor_date` = #{monitorDate} WHERE `id`=#{id}" })
	public void updateMonitorNodeHistory(MonitorNodeHistory monitorNodeHistory);
}
