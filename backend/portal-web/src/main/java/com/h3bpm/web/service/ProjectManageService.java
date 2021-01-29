package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.ProjectInfo;
import com.h3bpm.web.mapper.ProjectInfoMapper;
import com.h3bpm.web.mapper.UserMapper;
import com.h3bpm.web.vo.ProjectVo;
import com.h3bpm.web.vo.query.QueryLeaderActiveProjectInfo;
import com.h3bpm.web.vo.query.QueryProjectInfoList;

/**
 * @author 86135
 *
 */
@Service
public class ProjectManageService {
	@Autowired
	private ProjectInfoMapper projectInoMapper;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 分页查询项目信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<ProjectVo> findProjectInfoByPage(QueryProjectInfoList queryBean) {
		Page<ProjectVo> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<ProjectInfo> projectInfoList = projectInoMapper.findProjectInfo(queryBean.getKeyword(), queryBean.getCity(), queryBean.getLeaderId(), queryBean.getStartTimeStart(), queryBean.getStartTimeEnd(), queryBean.getEndTimeStart(), queryBean.getEndTimeEnd());

		List<ProjectVo> projectVoList = new ArrayList<ProjectVo>();
		if (projectInfoList != null) {
			for (ProjectInfo projectInfo : projectInfoList) {
				projectVoList.add(new ProjectVo(projectInfo));
			}
		}

		PageInfo<ProjectVo> pageInfo = new PageInfo<ProjectVo>(projectVoList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}

	/**
	 * 查询负责人在时间段内负责的项目信息列表
	 * 
	 * @param queryBean
	 */
	public List<ProjectVo> findLeadIdActiveProjectInfo(QueryLeaderActiveProjectInfo queryBean) {
		List<ProjectInfo> projectInfoList = projectInoMapper.findProjectInfoByLeaderIdAndStartTimeAndEndTime(queryBean.getLeaderId(), queryBean.getStartTime(), queryBean.getEndTime());

		List<ProjectVo> projectVoList = new ArrayList<ProjectVo>();
		if (projectInfoList != null) {
			for (ProjectInfo projectInfo : projectInfoList) {
				projectVoList.add(new ProjectVo(projectInfo));
			}
		}

		return projectVoList;
	}

	/**
	 * 新增项目信息
	 *
	 * @param knowledgeVo
	 * @return
	 */
	public String createProjectInfo(ProjectVo projectVo) {
		String uuid = projectVo.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			projectVo.setId(uuid);
		}

		if (projectVo.getLeaderName() == null || projectVo.getLeaderName().isEmpty()) {
			projectVo.setLeaderName(userMapper.getUserById(projectVo.getLeaderId()).getName());
		}

		projectInoMapper.createProjectInfo(new ProjectInfo(projectVo));
		return uuid;
	}

	/**
	 * 编辑项目信息
	 * 
	 * @param projectVo
	 */
	public void updateProjectInfo(ProjectVo projectVo) {

		if (projectVo.getLeaderName() == null || projectVo.getLeaderName().isEmpty()) {
			projectVo.setLeaderName(userMapper.getUserById(projectVo.getLeaderId()).getName());
		}

		projectInoMapper.updateProjectInfo(new ProjectInfo(projectVo));
	}

	/**
	 * 删除项目信息
	 * 
	 * @param projectVo
	 */
	public void deleteProjectInfo(String id) {
		projectInoMapper.deleteProjectInfo(id);
	}
}
