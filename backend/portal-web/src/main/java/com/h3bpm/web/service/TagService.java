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
	 * 根据Name获取文件
	 *
	 * @param tagName
	 * @return
	 */
	public List<Tag> findTagByName(String tagName) {
		List<Tag> tagList = null;
		try {
			tagList = tagMapper.findTagByName(tagName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tagList;
	}

	/**
	 * 新增标签
	 *
	 * @param tag
	 * @return 标签ID
	 */
	@Transactional
	public String createTag(Tag tag) {
		String uuid = tag.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			tag.setId(uuid);
		}

		tagMapper.createTag(tag);

		return uuid;
	}

}
