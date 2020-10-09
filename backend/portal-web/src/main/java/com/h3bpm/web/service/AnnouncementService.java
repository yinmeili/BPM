package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.entity.OrgInfo;
import com.h3bpm.web.mapper.AnnouncementMapper;
import com.h3bpm.web.mapper.OrgMapper;
import com.h3bpm.web.vo.AnnouncementVo;
import com.h3bpm.web.vo.query.QueryAnnouncementList;

@Service
public class AnnouncementService {
	@Autowired
	private AnnouncementMapper announcementMapper;

	@Autowired
	private OrgMapper orgMapper;

	/**
	 * 查询公告展示列表
	 *
	 * @param date
	 * @return
	 */
	public List<Announcement> findAnnouncementByTime(Date date) {

		List<Announcement> announcementList = null;
		try {
			announcementList = announcementMapper.findAnnouncementByTime(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return announcementList;
	}

	/**
	 * 分页查询公告列表
	 *
	 * @param queryBean
	 * @return
	 */
	public PageInfo<AnnouncementVo> findKnowledgeByPage(QueryAnnouncementList queryBean) {

		Page<Announcement> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<Announcement> announcementList = announcementMapper.findAnnouncementByPage(queryBean.getType(), queryBean.getTitle(), queryBean.getCreateTimeStart(), queryBean.getCreateTimeEnd(), queryBean.getOrgId());

		List<AnnouncementVo> respAnnouncementVoList = new ArrayList<AnnouncementVo>();
		if (announcementList != null) {
			for (Announcement announcement : announcementList) {
				respAnnouncementVoList.add(new AnnouncementVo(announcement));
			}
		}
		PageInfo<AnnouncementVo> pageInfo = new PageInfo<AnnouncementVo>(respAnnouncementVoList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}

	public Announcement getAnnouncementById(String id) {

		return announcementMapper.getAnnouncementById(id);
	}

	@Transactional
	public String createAnnouncement(AnnouncementVo announcementVo) {

		String uuid = announcementVo.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			announcementVo.setId(uuid);
		}

		if (announcementVo.getOrgId() != null) {
			OrgInfo orgInfo = orgMapper.getOrgById(announcementVo.getOrgId());
			announcementVo.setOrgName(orgInfo.getName());
		}

		announcementMapper.createAnnouncement(new Announcement(announcementVo));

		return uuid;
	}

	/**
	 * 根据流程ID更新Announcement
	 *
	 * @param announcementVo
	 */
	@Transactional
	public void updateAnnouncement(AnnouncementVo announcementVo) {

		if (announcementVo.getOrgId() != null) {
			OrgInfo orgInfo = orgMapper.getOrgById(announcementVo.getOrgId());
			announcementVo.setOrgName(orgInfo.getName());
		}

		announcementMapper.updateAnnouncement(new Announcement(announcementVo));
	}

	/**
	 * 根据流程ID删除Announcement
	 *
	 * @param id
	 */
	@Transactional
	public void deleteAnnouncement(String id) {

		announcementMapper.deleteAnnouncement(id);
	}
}
