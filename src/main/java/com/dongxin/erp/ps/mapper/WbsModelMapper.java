package com.dongxin.erp.ps.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.entity.WbsModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 工作分解模板
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
public interface WbsModelMapper extends BaseMapper<WbsModel> {

	//获取所有模板
	List<WbsModel> queryModelList(@Param("tenant") String tenant);
	
	List<ProjectCostInfo> queryWbsChildByParentId(@Param("id") String id,@Param("tenant") String tenant);
    
	//List<WbsModel> queryProjectWbslList(@Param("id") String id,@Param("tenant") String tenant);
	
	//根据模板ID获取所有子节点
	//List<WbsModel> selectWbsModelNodeByModelId(@Param("id") String id,@Param("tenant") String tenant);
}
