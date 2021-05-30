package com.dongxin.erp.ps.service;

import com.dongxin.erp.ps.entity.ProjectWbs;
import com.dongxin.erp.ps.mapper.ProjectWbsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;

/**
 * @Description: 项目工作分解结构
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Service
public class ProjectWbsService extends BaseService<ProjectWbsMapper, ProjectWbs> {

	@Autowired
	private ProjectWbsMapper projectWbsMapper;
	
	/**
	 * 根据项目ID获取wbs结构
	 * @param projectId
	 * @return
	 */
	public List<ProjectWbs> getProjectWbsByProjectId(String projectId) {
		return projectWbsMapper.getProjectWbsByProjectId(projectId,TenantContext.getTenant());
	}
	
	/**
	 * 根据项目ID删除wbs结构
	 * @param projectId 项目ID
	 */
	public void deleteByProjectId(String projectId) {
		List<ProjectWbs> projectWbsList = this.getProjectWbsByProjectId(projectId);
		for(ProjectWbs projectWbs:projectWbsList) {
			this.logicDelete(projectWbs);
		}
	}
}
