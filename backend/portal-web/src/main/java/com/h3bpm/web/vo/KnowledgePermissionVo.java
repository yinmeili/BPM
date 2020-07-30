package com.h3bpm.web.vo;

import com.h3bpm.web.entity.KnowledgePermission;

public class KnowledgePermissionVo {
	private String knowledgeId = null;
	private String[] orgs = null;

	@Deprecated
	public KnowledgePermissionVo(){
		
	}
	
	public KnowledgePermissionVo(KnowledgePermission knowledgePermission) {
		if (knowledgePermission != null) {
			String[] str = knowledgePermission.getOrgs().split(",");
			this.knowledgeId = knowledgePermission.getKnowledgeId();
			this.orgs = str;
		}
	}

	public KnowledgePermissionVo(String knowledgeId, String[] orgs) {
		this.knowledgeId = knowledgeId;
		this.orgs = orgs;
	}

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String[] getOrgs() {
		return orgs;
	}

	public void setOrgs(String[] orgs) {
		this.orgs = orgs;
	}
}
