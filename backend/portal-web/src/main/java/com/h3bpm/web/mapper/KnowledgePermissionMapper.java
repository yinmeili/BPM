package com.h3bpm.web.mapper;

import com.h3bpm.web.entity.KnowledgePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface KnowledgePermissionMapper {

    @Select("SELECT knowledge_id knowledgeId,orgs FROM `ot_knowledge_permission` where knowledge_id = #{knowledgeId}")
    public KnowledgePermission getKnowledgePermissionByKnowledgeId(@Param("knowledgeId") String knowledgeId);

    @Insert({"INSERT INTO `ot_knowledge_permission` (`knowledge_id`, `orgs`) VALUES (#{knowledgeId}, #{orgs})"})
    public void createKnowledgePermission(KnowledgePermission knowledgePermission);

//	@Insert({"INSERT INTO `h3bpm`.`ot_file_permission` (`file_id`) VALUES (#{fileId})"})
//	public void createFilePermissionTest(@Param("fileId") String fileId);//测试

    @Delete({"DELETE FROM ot_knowledge_permission WHERE knowledge_id = #{knowledgeId}"})
    public void deleteKnowledgePermissionByKnowledgeId(@Param("knowledgeId") String knowledgeId);
}

