package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.Liquidation;
import com.h3bpm.web.mapper.sqlprovider.LiquidationSqlProvider;

public interface LiquidationMapper {
	@Select("SELECT `id`, `name`, `ip_address` ipAddress, `operate_step` operateStep, `do_time` doTime, `status`,  `comment`, `index` from `h3bpm`.`ot_liquidation` WHERE `id` = #{id}")
	public Liquidation getLiquidationById(@Param("id") String id);

	@Insert({ "INSERT INTO `h3bpm`.`ot_liquidation` (`id`, `name`, `ip_address`, `operate_step`, `do_time`, `status`,  `comment`, `index`) VALUES (#{id}, #{name}, #{ipAddress}, #{operateStep}, #{doTime}, #{status}, #{comment}, #{index})" })
	public void createLiquidation(Liquidation liquidation);

	@Update({ "UPDATE `h3bpm`.`ot_liquidation` SET `name`= #{name}, `ip_address`=#{ipAddress}, `operate_step`=#{operateStep}, `do_time`=#{doTime}, `status`=#{status}, `comment`=#{comment}, `index`=#{index} WHERE `id`=#{id}" })
	public void updateLiquidation(Liquidation liquidation);

	@Delete({ "DELETE FROM ot_liquidation WHERE id = #{id}" })
	public void deleteLiquidation(@Param("id") String id);

	@SelectProvider(type = LiquidationSqlProvider.class, method = "findLiquidation")
	public List<Liquidation> findLiquidation(@Param("keyword") String keyword, @Param("status") Integer status);

	@Select("SELECT COUNT(1) from `h3bpm`.`ot_liquidation` WHERE `index`=#{index}")
	public int getCountByIndex(@Param("index") int index);

	@SelectProvider(type = LiquidationSqlProvider.class, method = "updateIndexAddOne")
	public void updateIndexAddOne(@Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);

	@SelectProvider(type = LiquidationSqlProvider.class, method = "updateIndexRemoveOne")
	public void updateIndexRemoveOne(@Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);

	@Select("SELECT MAX(`index`) from `h3bpm`.`ot_liquidation`")
	public Integer getMaxIndex();
}
