package com.h3bpm.web.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.MyKnowledge;
import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.enumeration.TagType;
import com.h3bpm.web.mapper.MyKnowledgeMapper;
import com.h3bpm.web.vo.MyKnowledgeVo;
import com.h3bpm.web.vo.query.QueryMyKnowledgeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MyKnowledgeService {
    @Autowired
    private MyKnowledgeMapper myKnowledgeMapper;

    @Autowired
    private TagService tagService;

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

    public MyKnowledge getMyKnowledgeById(String id){
        return myKnowledgeMapper.getMyKnowledgeById(id);
    }

    /**
     * 分页查询知识库信息
     *
     * @param queryBean
     */
    public PageInfo<MyKnowledgeVo> findKnowledgeByPage(QueryMyKnowledgeList queryBean) {
        Page<MyKnowledge> page = PageHelper.startPage(queryBean.getiDisplayStart(), queryBean.getiDisplayLength());
        List<MyKnowledge> myKnowledgeList = myKnowledgeMapper.findMyKnowledge(queryBean.getName(), queryBean.getTagName(), queryBean.getFlowCodes(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd(), queryBean.getEndTimeStart(), queryBean.getEndTimeEnd());

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


}
