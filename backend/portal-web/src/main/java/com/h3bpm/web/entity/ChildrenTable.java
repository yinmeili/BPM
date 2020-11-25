package com.h3bpm.web.entity;

public class ChildrenTable {
	private String objectID = null;
	private int parentIndex = 0;
	private String parentPropertyName = null;
	private String parentObjectID = null;

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public int getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}

	public String getParentPropertyName() {
		return parentPropertyName;
	}

	public void setParentPropertyName(String parentPropertyName) {
		this.parentPropertyName = parentPropertyName;
	}

	public String getParentObjectID() {
		return parentObjectID;
	}

	public void setParentObjectID(String parentObjectID) {
		this.parentObjectID = parentObjectID;
	}
}
