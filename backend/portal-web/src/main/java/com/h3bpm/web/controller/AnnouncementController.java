package com.h3bpm.web.controller;

import OThinker.Common.Organization.Models.User;
import OThinker.H3.Controller.ControllerBase;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.Announcement;
import com.h3bpm.web.enumeration.AnnouncementType;
import com.h3bpm.web.service.AnnouncementService;
import com.h3bpm.web.vo.*;
import com.h3bpm.web.vo.query.QueryAnnouncementList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/Portal/announcement")
public class AnnouncementController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

	@Autowired
	private AnnouncementService announcementService;

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/findShowAll", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo findShowAll() throws IOException {

		List<RespFindShowAllVo> list = new ArrayList<>();

		List<Announcement> announcementList = announcementService.findAnnouncementByTime(new Date());
		if (announcementList != null) {
			for (Announcement announcement : announcementList) {
				list.add(new RespFindShowAllVo(announcement));
			}
		}

		return new ResponseVo(list);
	}

	@RequestMapping(value = "/listAnnouncementByPage", produces = "application/json;charset=utf8")
	@ResponseBody
	public RespPageVo listAnnouncementByPage(@ModelAttribute ReqListAnnouncementPageVo requestBean) {

		QueryAnnouncementList queryAnnouncementList = new QueryAnnouncementList(requestBean);

		PageInfo<AnnouncementVo> pageInfo = announcementService.findKnowledgeByPage(queryAnnouncementList);

		return new RespPageVo(requestBean.getsEcho(), pageInfo.getTotal(), pageInfo.getList());

	}

	@RequestMapping(value = "/createAnnouncement", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo createAnnouncement(@RequestBody ReqCreateAnnouncementVo reqCreateAnnouncementVo) throws Exception {
		Map<String, Object> userMap = this._getCurrentUser();
		User user = (User) userMap.get("User");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // String与Date之间进行相互转换
		AnnouncementVo announcementVo = new AnnouncementVo();

		announcementVo.setCreateUserId(user.getObjectID()); // 存入用户id
		announcementVo.setTitle(reqCreateAnnouncementVo.getTitle());
		announcementVo.setDescription(reqCreateAnnouncementVo.getDescription());
		announcementVo.setLink(reqCreateAnnouncementVo.getLink());
		announcementVo.setType(reqCreateAnnouncementVo.getType());
		announcementVo.setOrgId(reqCreateAnnouncementVo.getOrgId());

		try {
			announcementVo.setStartTime(format.parse(reqCreateAnnouncementVo.getStartTime()).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			announcementVo.setEndTime(format.parse(reqCreateAnnouncementVo.getEndTime()).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		announcementVo.setCreateTime(new Date().getTime());
		announcementService.createAnnouncement(announcementVo);

		return new ResponseVo("创建成功");
	}

	@RequestMapping(value = "/updateAnnouncement", produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo updateAnnouncement(@RequestBody ReqUpdateAnnouncementVo reqUpdateAnnouncementVo) throws Exception {
		Map<String, Object> userMap = this._getCurrentUser();
		User user = (User) userMap.get("User");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // String与Date之间进行相互转换

		Announcement announcement = announcementService.getAnnouncementById(reqUpdateAnnouncementVo.getId());
		AnnouncementVo announcementVo = new AnnouncementVo(announcement);

		announcementVo.setUpdateUserId(user.getObjectID());
		announcementVo.setTitle(reqUpdateAnnouncementVo.getTitle());
		announcementVo.setDescription(reqUpdateAnnouncementVo.getDescription());
		announcementVo.setLink(reqUpdateAnnouncementVo.getLink());
		announcementVo.setType(reqUpdateAnnouncementVo.getType());
		announcementVo.setOrgId(reqUpdateAnnouncementVo.getOrgId());

		try {
			announcementVo.setStartTime(format.parse(reqUpdateAnnouncementVo.getStartTime()).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			announcementVo.setEndTime(format.parse(reqUpdateAnnouncementVo.getEndTime()).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		announcementVo.setUpdateTime(new Date().getTime());
		announcementService.updateAnnouncement(announcementVo);

		return new ResponseVo("修改成功");
	}

	@RequestMapping(value = "/deleteAnnouncement", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public ResponseVo deleteAnnouncement(@RequestParam("id") String id) {
		announcementService.deleteAnnouncement(id);
		return new ResponseVo("删除成功");
	}
}
