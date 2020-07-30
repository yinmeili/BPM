package com.h3bpm.web.service;

import com.h3bpm.web.entity.KnowledgePermission;
import com.h3bpm.web.mapper.KnowledgePermissionMapper;
import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.FilePermissionVo;
import com.h3bpm.web.vo.KnowledgePermissionVo;
import com.h3bpm.web.vo.OrgInfoVo;
import com.h3bpm.web.vo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
