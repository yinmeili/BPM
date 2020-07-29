package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.mapper.KnowledgeMapper;
import com.h3bpm.web.vo.KnowledgeVo;
import com.h3bpm.web.vo.query.QueryKnowledgeList;

/**
 * @author 86135
 *
 */
@Service
public class KnowledgeService {

	@Autowired
	private KnowledgeMapper knowledgeMapper;

	@Autowired
	private FilePermissionMapper filePermissionMapper;
	
	@Autowired
	private FilePermissionService filePermissionService;

	/**
	 * 新增Knowledge
	 *
	 * @param knowledgeVo
	 * @return
	 */
	@Transactional
	public String createKnowledge(KnowledgeVo knowledgeVo) {
		String uuid = knowledgeVo.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			knowledgeVo.setId(uuid);
		}

		knowledgeMapper.createKnowledge(new Knowledge(knowledgeVo));

		// 与权限相关的东西---->不是很了解
		if (knowledgeVo.getPermission() != null) {
			knowledgeVo.getPermission().setFileId(uuid);
			filePermissionMapper.createFilePermission(new FilePermission(knowledgeVo.getPermission()));
		}
		return uuid;
	}

	@Transactional
	public void updateKnowledge(KnowledgeVo knowledgeVo) {
		knowledgeMapper.updateKnowledge(new Knowledge(knowledgeVo));

		if (knowledgeVo.getPermission() != null) {
			filePermissionMapper.deleteFilePermissionByFileId(knowledgeVo.getId());

			if (knowledgeVo.getPermission().getFileId() == null) {
				knowledgeVo.getPermission().setFileId(knowledgeVo.getId());
			}
			filePermissionMapper.createFilePermission(new FilePermission(knowledgeVo.getPermission()));
		}
	}

	public Knowledge getKnowledgeById(String id) {
		return knowledgeMapper.getKnowledgeById(id);
	}

	/**
	 * 分页查询知识库信息
	 * 
	 * @param knowledgeVo
	 */
	public PageInfo<KnowledgeVo> findKnowledgeByPage(QueryKnowledgeList queryBean) {
		Page<Knowledge> page = PageHelper.startPage(queryBean.getiDisplayStart(), queryBean.getiDisplayLength());
		List<Knowledge> knowledgeList = knowledgeMapper.findKnowledge(queryBean.getName(), queryBean.getTagName(), queryBean.getFlowCodes(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd(), queryBean.getEndTimeStart(), queryBean.getEndTimeEnd());

		List<KnowledgeVo> knowledgeVoList = new ArrayList<KnowledgeVo>();
		if(knowledgeList != null){
			for(Knowledge knowledge:knowledgeList){
				knowledgeVoList.add(new KnowledgeVo(knowledge));
			}
		}
		PageInfo<KnowledgeVo> pageInfo = new PageInfo<KnowledgeVo>(knowledgeVoList);
		pageInfo.setTotal(page.getTotal());
		
		return pageInfo;
	}

}
