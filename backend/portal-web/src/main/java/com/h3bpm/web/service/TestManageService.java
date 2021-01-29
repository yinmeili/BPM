package com.h3bpm.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.h3bpm.web.entity.TestEnvInfo;
import com.h3bpm.web.mapper.TestEnvMapper;
import com.h3bpm.web.vo.TestEnvVo;
import com.h3bpm.web.vo.query.QueryTestEnvList;

@Service
public class TestManageService {

	@Autowired
	private TestEnvMapper testEnvMapper;

	/**
	 * 新增Knowledge
	 *
	 * @param knowledgeVo
	 * @return
	 */
	@Transactional
	public String createTestEnv(TestEnvVo voBean) {
		String uuid = voBean.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			voBean.setId(uuid);
		}
		testEnvMapper.createTestEnv(new TestEnvInfo(voBean));

		return uuid;
	}

	public void updateKnowledge(TestEnvVo voBean) {
		testEnvMapper.updateTestEnv(new TestEnvInfo(voBean));
	}

	/**
	 * 分页查询知识库信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<TestEnvVo> findTestEnvByPage(QueryTestEnvList queryBean) {
		Page<TestEnvInfo> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<TestEnvInfo> testEnvList = testEnvMapper.findTestEnv(queryBean.getKeyword());

		List<TestEnvVo> testEnvVoList = new ArrayList<TestEnvVo>();
		if (testEnvList != null) {
			for (TestEnvInfo testEnv : testEnvList) {
				testEnvVoList.add(new TestEnvVo(testEnv));
			}
		}
		PageInfo<TestEnvVo> pageInfo = new PageInfo<TestEnvVo>(testEnvVoList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}

	public List<TestEnvVo> findAllTestEnv() {
		List<TestEnvInfo> testEnvList = testEnvMapper.findTestEnv(null);

		List<TestEnvVo> testEnvVoList = new ArrayList<TestEnvVo>();
		if (testEnvList != null) {
			for (TestEnvInfo testEnv : testEnvList) {
				testEnvVoList.add(new TestEnvVo(testEnv));
			}
		}

		return testEnvVoList;
	}

	public TestEnvVo getTestEnvById(String id) {
		TestEnvInfo testEnv = testEnvMapper.getTestEnvById(id);
		
		if (testEnv != null) {
			return new TestEnvVo(testEnv);
		}
		
		return null;
	}

	/**
	 * 删除TestEnv
	 *
	 * @param testEnvId
	 */
	public void deleteTestEnv(String testEnvId) {
		testEnvMapper.deleteTestEnv(testEnvId);
	}

}
