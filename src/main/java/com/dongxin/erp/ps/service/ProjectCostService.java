package com.dongxin.erp.ps.service;

import com.dongxin.erp.bd.entity.WbsType;
import com.dongxin.erp.bd.service.WbsTypeService;
import com.dongxin.erp.ps.entity.ProjectCost;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.mapper.ProjectCostMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 项目费用表
 * @Author: jeecg-boot/huangheng
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class ProjectCostService extends BaseService<ProjectCostMapper, ProjectCost>{

	@Autowired
	private ProjectCostMapper projectCostMapper;
	
	@Autowired
	private WbsTypeService wbsTypeService;
	
	public List<ProjectCost> selectByMainId(String mainId) {
		return baseMapper.selectByMainId(mainId);
	}

	public void deleteBatchByMainId(String id) {
        List<ProjectCost> details = selectByMainId(id);
        for(ProjectCost detail : details){
            logicDelete(detail);
        }
    }
	
	/**
	 * 根据项目ID获取费用明细
	 * @param projectId
	 * @return
	 */
	public List<ProjectCostInfo> getProjectCostByProjectId(String projectId){
		return projectCostMapper.getProjectCostByProjectId(projectId,TenantContext.getTenant());
	}
	
	/**
	 * 封装费用类型
	 * @param projectCostList
	 * @return
	 */
	public List<ProjectCost> getWbsTypeName(List<ProjectCost> projectCostList){
		for(ProjectCost projectCost:projectCostList) {
		
			WbsType wbsType = wbsTypeService.getById(projectCost.getWbsTypeId());
			if(null != wbsType) {
				projectCost.setWbsTypeName(wbsType.getWbsTypeName());
			}
		}
		return projectCostList;
	}
	
	/**
	 * 获取记录
	 * @param id
	 * @return
	 */
	public ProjectCost selectCostById(String id){
		return projectCostMapper.selectCostById(id);
	}

	/**
	 * 更新删除标识
	 * @param id
	 */
	public void updateDelFlag(String id) {
		projectCostMapper.updateDelFlag(id);
	}
}
