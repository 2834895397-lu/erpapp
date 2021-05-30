package com.dongxin.erp.bd.mapper;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bd.entity.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 物料信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */

public interface MaterialMapper extends BaseMapper<Material> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

    List<Map<String, String>> selectUnit(@Param("tenant") String tenant);


	List<Material> selectMaterialId(@Param("orgId") String orgId);

	List<Material> selectMaterialPid(@Param(("materialId")) String materialId);
}
