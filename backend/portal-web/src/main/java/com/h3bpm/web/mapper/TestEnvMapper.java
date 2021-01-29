package com.h3bpm.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.h3bpm.web.entity.TestEnvInfo;
import com.h3bpm.web.mapper.sqlprovider.TestEnvSqlProvider;

public interface TestEnvMapper {

	@SelectProvider(type = TestEnvSqlProvider.class, method = "findTestEnv")
	public List<TestEnvInfo> findTestEnv(@Param("keyword") String keyword);

	@Select("SELECT  a.id, a.name,a.`desc`, a.`join_address` joinAddress, a.vm_datas vmDatas, a.env_datas envDatas, a.env_type envType, a.system_name systemName, a.create_time createTime FROM ow_test_env a  where a.id = #{id}")
	public TestEnvInfo getTestEnvById(@Param("id") String id);

	@Insert({ "INSERT INTO `h3bpm`.`ow_test_env` (`id`, `name`, `desc`, `join_address`, `env_type`, `system_name`, `vm_datas`, `env_datas`) VALUES (#{id},  #{name}, #{desc}, #{joinAddress}, #{envType}, #{systemName}, #{vmDatas}, #{envDatas})" })
	public void createTestEnv(TestEnvInfo testEnvInfo);

	@Update({ "UPDATE `h3bpm`.`ow_test_env` SET `name`=#{name}, `desc`=#{desc}, `join_address`=#{joinAddress}, `env_type`=#{envType}, `system_name`=#{systemName}, `vm_datas`=#{vmDatas},`env_datas`=#{envDatas} WHERE `id`=#{id}" })
	public void updateTestEnv(TestEnvInfo testEnvInfo);

	@Delete({ "DELETE FROM `h3bpm`.`ow_test_env` WHERE id = #{id}" })
	public void deleteTestEnv(@Param("id") String id);

}
