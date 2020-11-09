package com.h3bpm.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.h3bpm.web.mapper.KnowledgePermissionMapper;
import com.h3bpm.web.vo.KnowledgePermissionVo;

@Service
public class KnowledgePermissionService {

    @Autowired
    private KnowledgePermissionMapper knowledgePermissionMapper;

    public KnowledgePermissionVo getKnowledgePermissionByKnowledgeId(String knowlwdgeId) {
        return new KnowledgePermissionVo(knowledgePermissionMapper.getKnowledgePermissionByKnowledgeId(knowlwdgeId));
    }

    public void createKnowledgePermission(KnowledgePermissionVo knowledgePermissionVo) {
        knowledgePermissionMapper.createKnowledgePermission(new com.h3bpm.web.entity.KnowledgePermission(knowledgePermissionVo));
    }

    public void deleteKnowledgePermissionByKnowledgeId(String knowlwdgeId) {
        knowledgePermissionMapper.deleteKnowledgePermissionByKnowledgeId(knowlwdgeId);
    }

}
