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
import com.h3bpm.web.entity.Liquidation;
import com.h3bpm.web.mapper.LiquidationMapper;
import com.h3bpm.web.vo.LiquidationVo;
import com.h3bpm.web.vo.query.QueryLiquidationList;

@Service
public class LiquidationService extends ApiDataService {

	@Autowired
	private LiquidationMapper liquidationMapper;

	@Transactional
	public String createLiquidation(LiquidationVo liquidationVo) {

		String uuid = liquidationVo.getId();
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			liquidationVo.setId(uuid);
		}

		Integer maxIndex = liquidationMapper.getMaxIndex();
		if (maxIndex == null) {
			maxIndex = 0;
		}

		int newIndex = liquidationVo.getIndex();
		if (newIndex > (maxIndex + 1)) {
			newIndex = maxIndex + 1;

			liquidationVo.setIndex(newIndex);
		}

		/*
		 * 先更新序列号，再做新增操作
		 */
		int indexCount = liquidationMapper.getCountByIndex(newIndex);
		if (indexCount != 0) {
			liquidationMapper.updateIndexAddOne(newIndex, null);
		}

		liquidationMapper.createLiquidation(new Liquidation(liquidationVo));

		return uuid;
	}

	/**
	 * 根据ID更新Liquidation
	 *
	 * @param announcementVo
	 */
	@Transactional
	public void updateLiquidation(LiquidationVo liquidationVo) {
		Liquidation liquidationDb = liquidationMapper.getLiquidationById(liquidationVo.getId());
		int oldIndex = liquidationDb.getIndex();
		int newIndex = liquidationVo.getIndex();
		int indexCount = 0;

		/*
		 * 更新序号，序号存在时，如果小序号改大，则把小序号和大序号之间的记录序号都-1；如果大序号改小，则把小序号和大序号之间的序号+1
		 */
		if (oldIndex != newIndex) {
			int maxIndex = liquidationMapper.getMaxIndex();
			if (newIndex > maxIndex) {
				newIndex = maxIndex;
			}

			liquidationVo.setIndex(newIndex);

			indexCount = liquidationMapper.getCountByIndex(newIndex);
		}

		if (indexCount != 0) {

			if (oldIndex > newIndex) {
				liquidationMapper.updateIndexAddOne(newIndex, oldIndex);

			} else if (oldIndex < newIndex) {

				liquidationMapper.updateIndexRemoveOne(oldIndex, newIndex);
			}
		}

		liquidationMapper.updateLiquidation(new Liquidation(liquidationVo));
	}

	/**
	 * 根据ID删除Liquidation
	 *
	 * @param id
	 */
	@Transactional
	public void deleteLiquidation(String id) {

		/*
		 * 将删除记录之后的序号都-1
		 */
		Liquidation liquidationDb = liquidationMapper.getLiquidationById(id);
		liquidationMapper.updateIndexRemoveOne(liquidationDb.getIndex(), null);

		liquidationMapper.deleteLiquidation(id);
	}

	/**
	 * 分页查询闭市流程模板信息
	 * 
	 * @param queryBean
	 */
	public PageInfo<LiquidationVo> findLiquidationByPage(QueryLiquidationList queryBean) {
		Page<Liquidation> page = PageHelper.startPage(queryBean.getPageNum(), queryBean.getiDisplayLength());
		List<Liquidation> liquidationList = liquidationMapper.findLiquidation(queryBean.getKeyword(), queryBean.getStatus());

		List<LiquidationVo> liquidationVoList = new ArrayList<LiquidationVo>();
		if (liquidationList != null) {
			for (Liquidation liquidation : liquidationList) {
				liquidationVoList.add(new LiquidationVo(liquidation));
			}
		}
		PageInfo<LiquidationVo> pageInfo = new PageInfo<LiquidationVo>(liquidationVoList);
		pageInfo.setTotal(page.getTotal());

		return pageInfo;
	}

}
