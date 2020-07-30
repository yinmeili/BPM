package com.h3bpm.web.controller;

import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.enumeration.TagType;
import com.h3bpm.web.service.TagService;
import com.h3bpm.web.vo.RespListKnowledgeTagByNameVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/Portal/tag")
public class TagManagerController extends ControllerBase {

	private static final Logger logger = LoggerFactory.getLogger(TagManagerController.class);

	@Autowired
	private TagService tagService;

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/listKnowledgeTagByName", method = RequestMethod.GET, produces = "application/json;charset=utf8")
	@ResponseBody
	public List<RespListKnowledgeTagByNameVo> listKnowledgeTagByName(@RequestParam(required = false, name = "key") String key) throws IOException {

		List<RespListKnowledgeTagByNameVo> list = new ArrayList<>();

		List<Tag> tagList = tagService.findTagByTypeAndName(key, TagType.KNOWLEDGE.getValue());
		for (Tag tag : tagList) {
			list.add(new RespListKnowledgeTagByNameVo(tag));
		}

		return list;
	}

	@Override
	public String getFunctionCode() {
		// TODO Auto-generated method stub
		return null;
	}

}
