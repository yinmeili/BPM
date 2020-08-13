package com.h3bpm.web.vo;

import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.enumeration.KnowledgeStatus;

import java.util.Date;

/**
 * Created by tonghao on 2020/3/1.
 */
public class KnowledgeVo {

	// list
	private String id;
	private String flowId;
	private String name;
	private String desc;
	private String tagName;
	private Date startTime;
	private Date endTime;
	private String createUserId;
	private Date createTime;
	private String createUserName;
	private String flowCodeDesc;
	private KnowledgePermissionVo permission = null;
	private int status = 1;

	public KnowledgeVo() {
	}

	public KnowledgeVo(Knowledge knowledge){
		this.id = knowledge.getId();
		this.flowId = knowledge.getFlowId();
		this.name = knowledge.getName();
		this.desc = knowledge.getDesc();
		this.tagName = knowledge.getTagName();
		this.startTime = knowledge.getStartTime();
		this.endTime = knowledge.getEndTime();
		this.createUserId = knowledge.getCreateUserId();
		this.createTime = knowledge.getCreateTime();
		this.createUserName = knowledge.getCreateUserName();
		this.flowCodeDesc = knowledge.getFlowCodeDesc();
		this.status = knowledge.getStatus();
	}

	public KnowledgeVo(MyKnowledge myKnowledge){
		this.flowId = myKnowledge.getFlowId();
		this.name = myKnowledge.getName();
		this.desc = myKnowledge.getDesc();
		this.tagName = myKnowledge.getTagName();
		this.startTime = myKnowledge.getStartTime();
		this.endTime = myKnowledge.getEndTime();
		this.createUserId = myKnowledge.getCreateUserId();
		this.createTime = myKnowledge.getCreateTime();
		this.createUserName = myKnowledge.getCreateUserName();
		this.flowCodeDesc = myKnowledge.getFlowCodeDesc();
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getFlowCodeDesc() {
		return flowCodeDesc;
	}

	public void setFlowCodeDesc(String flowCodeDesc) {
		this.flowCodeDesc = flowCodeDesc;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public KnowledgePermissionVo getPermission() {
		return permission;
	}

	public void setPermission(KnowledgePermissionVo permission) {
		this.permission = permission;
	}

	public String getStatusStr(){
		return KnowledgeStatus.parse(this.status).getDisplayName();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
