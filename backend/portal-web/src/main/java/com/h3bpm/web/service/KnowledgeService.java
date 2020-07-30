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
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.entity.KnowledgePermission;
import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.enumeration.TagType;
import com.h3bpm.web.mapper.KnowledgeMapper;
import com.h3bpm.web.mapper.KnowledgePermissionMapper;
import com.h3bpm.web.vo.KnowledgeVo;
import com.h3bpm.web.vo.query.QueryKnowledgeList;

@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private KnowledgePermissionMapper knowledgePermissionMapper;

    @Autowired
    private TagService tagService;
    /**
     * 新增Knowledge
     *
     * @param knowledgeVo
     * @return
     */
    @Transactional
    public String createKnowledge(KnowledgeVo knowledgeVo){
        String uuid = knowledgeVo.getId();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            knowledgeVo.setId(uuid);
        }

        knowledgeMapper.createKnowledge(new Knowledge(knowledgeVo));

        //对tag的处理
        if(tagService.getTagByTypeAndName(knowledgeVo.getTagName(), TagType.KNOWLEDGE.getValue()) == null){
            Tag tag = new Tag();
            tag.setName(knowledgeVo.getTagName());
            tag.setType(TagType.KNOWLEDGE.getValue());
            tagService.createTag(tag);
        }
        if (knowledgeVo.getPermission() != null) {
            knowledgeVo.getPermission().setKnowledgeId(uuid);
            knowledgePermissionMapper.createKnowledgePermission(new KnowledgePermission(knowledgeVo.getPermission()));
        }

        return uuid;
    }

    @Transactional
    public void updateKnowledge(KnowledgeVo knowledgeVo){
        knowledgeMapper.updateKnowledge(new Knowledge(knowledgeVo));

        //对tag的处理
        if(tagService.getTagByTypeAndName(knowledgeVo.getTagName(), TagType.KNOWLEDGE.getValue()) == null){
            Tag tag = new Tag();
            tag.setName(knowledgeVo.getTagName());
            tag.setType(TagType.KNOWLEDGE.getValue());
            tagService.createTag(tag);
        }

        if (knowledgeVo.getPermission() != null) {
            knowledgePermissionMapper.deleteKnowledgePermissionByKnowledgeId(knowledgeVo.getId());

            if (knowledgeVo.getPermission().getKnowledgeId() == null) {
                knowledgeVo.getPermission().setKnowledgeId(knowledgeVo.getId());
            }
            knowledgePermissionMapper.createKnowledgePermission(new KnowledgePermission(knowledgeVo.getPermission()));
        }
    }

    public Knowledge getKnowledgeById(String id){
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
