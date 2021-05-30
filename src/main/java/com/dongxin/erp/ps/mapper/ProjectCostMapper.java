package com.dongxin.erp.ps.mapper;

import java.util.List;
import com.dongxin.erp.ps.entity.ProjectCost;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 项目费用表
 * @Author: jeecg-boot
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface ProjectCostMapper extends BaseMapper<ProjectCost> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ProjectCost> selectByMainId(@Param("mainId") String mainId);
	
	ProjectCost selectCostById(@Param("id") String id);
	
	public List<ProjectCostInfo> getProjectCostByProjectId(@Param("projectId") String projectId,@Param("tenant") String tenant);

	void updateDelFlag(@Param("id") String id);
}
