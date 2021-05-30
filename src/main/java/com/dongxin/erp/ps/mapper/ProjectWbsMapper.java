package com.dongxin.erp.ps.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.ps.entity.ProjectWbs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目工作分解结构
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
public interface ProjectWbsMapper extends BaseMapper<ProjectWbs> {

	List<ProjectWbs> getProjectWbsByProjectId(@Param("projectId") String projectId,@Param("tenant") String tenant);
}
