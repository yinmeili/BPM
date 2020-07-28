package com.h3bpm.web.service;

import com.h3bpm.web.entity.File;
import com.h3bpm.web.entity.FilePermission;
import com.h3bpm.web.entity.Knowledge;
import com.h3bpm.web.mapper.FilePermissionMapper;
import com.h3bpm.web.mapper.KnowledgeMapper;
import com.h3bpm.web.vo.KnowledgeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private FilePermissionMapper filePermissionMapper;
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

        //与权限相关的东西---->不是很了解
        if (knowledgeVo.getPermission() != null) {
            knowledgeVo.getPermission().setFileId(uuid);
            filePermissionMapper.createFilePermission(new FilePermission(knowledgeVo.getPermission()));
        }
        return uuid;
    }

    @Transactional
    public void updateKnowledge(KnowledgeVo knowledgeVo){
        knowledgeMapper.updateKnowledge(new Knowledge(knowledgeVo));

        if (knowledgeVo.getPermission() != null) {
            filePermissionMapper.deleteFilePermissionByFileId(knowledgeVo.getId());

            if (knowledgeVo.getPermission().getFileId() == null) {
                knowledgeVo.getPermission().setFileId(knowledgeVo.getId());
            }
            filePermissionMapper.createFilePermission(new FilePermission(knowledgeVo.getPermission()));
        }
    }

    public Knowledge getKnowledgeById(String id){
        return knowledgeMapper.getKnowledgeById(id);
    }

}
