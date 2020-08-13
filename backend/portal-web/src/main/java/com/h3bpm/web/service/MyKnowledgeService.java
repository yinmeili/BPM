package com.h3bpm.web.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.FlowCode;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.enumeration.TagType;
import com.h3bpm.web.mapper.KnowledgeMapper;
import com.h3bpm.web.mapper.MyKnowledgeMapper;
import com.h3bpm.web.vo.MyKnowledgeVo;
import com.h3bpm.web.vo.ResponseVo;
import com.h3bpm.web.vo.query.QueryMyKnowledgeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MyKnowledgeService {
    @Autowired
    private MyKnowledgeMapper myKnowledgeMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    /**
     * 新增MyKnowldge
     *
     * @param myKnowledgeVo
     * @return
     */
    @Transactional
    public String createMyKnowledge(MyKnowledgeVo myKnowledgeVo){
        String uuid = myKnowledgeVo.getId();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            myKnowledgeVo.setId(uuid);
        }

        myKnowledgeMapper.createMyKnowledge(new MyKnowledge(myKnowledgeVo));

        //对tag的处理
        if(tagService.getTagByTypeAndName(myKnowledgeVo.getTagName(), TagType.KNOWLEDGE.getValue()) == null){
            Tag tag = new Tag();
            tag.setName(myKnowledgeVo.getTagName());
            tag.setType(TagType.KNOWLEDGE.getValue());
            tagService.createTag(tag);
        }
        return uuid;
    }

    @Transactional
    public void updateMyKnowledge(MyKnowledgeVo myKnowledgeVo){
        myKnowledgeMapper.updateMyKnowledge(new MyKnowledge(myKnowledgeVo));

        //对tag的处理
        if(tagService.getTagByTypeAndName(myKnowledgeVo.getTagName(), TagType.KNOWLEDGE.getValue()) == null){
            Tag tag = new Tag();
            tag.setName(myKnowledgeVo.getTagName());
            tag.setType(TagType.KNOWLEDGE.getValue());
            tagService.createTag(tag);
        }

    }

    @Transactional
    public String collectToMyKnowledge(String knowledgeId, String createUserId, String createUserName){
        Knowledge knowledge = knowledgeMapper.getKnowledgeById(knowledgeId);
        MyKnowledge myKnowledge = new MyKnowledge(knowledge);
        myKnowledge.setCreateUserId(createUserId);
        myKnowledge.setCreateUserName(createUserName);
        myKnowledge.setCreateTime(new Date());
        String uuid = myKnowledge.getId();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            myKnowledge.setId(uuid);
            System.out.println(uuid);
        }
        myKnowledgeMapper.createMyKnowledge(myKnowledge);

        return uuid;
    }


    @Transactional
    public String collectFlowToMyKnowledge(MyKnowledgeVo myKnowledgeVo){
        String uuid = myKnowledgeVo.getId();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
            myKnowledgeVo.setId(uuid);
        }
        MyKnowledge myKnowledge = new MyKnowledge(myKnowledgeVo);
        FlowCode flowCode = knowledgeMapper.getFlowCodeByFlowId(myKnowledgeVo.getFlowId());
        myKnowledge.setFlowCode(flowCode.getFlowCode());
        myKnowledge.setFlowCodeDesc(flowCode.getFlowCodeDesc());
        myKnowledgeMapper.createMyKnowledge(myKnowledge);

        return uuid;
    }

    public MyKnowledge getMyKnowledgeById(String id){
        return myKnowledgeMapper.getMyKnowledgeById(id);
    }

    /**
     * 分页查询知识库信息
     *
     * @param queryBean
     */
    public PageInfo<MyKnowledgeVo> findMyKnowledgeByPage(QueryMyKnowledgeList queryBean) {
        Page<MyKnowledge> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
        List<MyKnowledge> myKnowledgeList = myKnowledgeMapper.findMyKnowledge(queryBean.getName(), queryBean.getTagName(), queryBean.getFlowCodes(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd(), queryBean.getEndTimeStart(), queryBean.getEndTimeEnd(), queryBean.getQueryUserId());

        List<MyKnowledgeVo> myKnowledgeVoList = new ArrayList<MyKnowledgeVo>();
        if(myKnowledgeList != null){
            for(MyKnowledge myKnowledge : myKnowledgeList){
                myKnowledgeVoList.add(new MyKnowledgeVo(myKnowledge));
            }
        }
        PageInfo<MyKnowledgeVo> pageInfo = new PageInfo<MyKnowledgeVo>(myKnowledgeVoList);
        pageInfo.setTotal(page.getTotal());

        return pageInfo;
    }


    /**
     * 删除Knowledge
     *
     * @param knowledgeId
     */
    @Transactional
    public void deleteMyKnowledge(String knowledgeId) {
        MyKnowledge myKnowledge = myKnowledgeMapper.getMyKnowledgeById(knowledgeId);
        myKnowledge.setDelete(true);
        myKnowledge.setDeleteTime(new Date());

        myKnowledgeMapper.updateMyKnowledge(myKnowledge);
    }


}
