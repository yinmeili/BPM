package com.h3bpm.web.service;

import com.h3bpm.web.entity.Tag;
import com.h3bpm.web.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TagService extends ApiDataService {
	@Autowired
	private TagMapper tagMapper;

	/**
	 * 根据Type和like Name获取标签
	 *
	 * @param tagName
	 * @param tagType
	 * @return
	 */
	public List<Tag> findTagByTypeAndName(String tagName, String tagType) {
		List<Tag> tagList = null;
		try {
			tagList = tagMapper.findTagByTypeAndName(tagName, tagType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tagList;
	}

	/**
	 * 根据Type和指定Name获取标签
	 *
	 * @param tagName
	 * @param tagType
	 * @return
	 */
	public Tag getTagByTypeAndName(String tagName, String tagType) {

		if (tagName == null || tagName.isEmpty()){
			return null;
		}
		if (tagType == null || tagType.isEmpty()){
			return null;
		}
		return tagMapper.getTagByTypeAndName(tagName, tagType);
	}

	/**
	 * 新增标签
	 *
	 * @param tag
	 * @return 标签ID
	 */
	@Transactional
	public String createTag(Tag tag) {
		if(tag.getName() == null || tag.getName().isEmpty())
			return null;

		String uuid = tag.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			tag.setId(uuid);
		}

		tagMapper.createTag(tag);

		return uuid;
	}

}
