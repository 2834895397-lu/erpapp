package com.dongxin.erp.ps.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.mm.entity.SettleDtl;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.entity.WbsModel;
import com.dongxin.erp.ps.mapper.WbsModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;

/**
 * @Description: 工作分解模板
 * @Author: jeecg-boot
 * @Date:   2021-01-20
 * @Version: V1.0
 */
@Service
public class WbsModelService extends BaseService<WbsModelMapper, WbsModel> {

	@Autowired
	private WbsModelMapper wbsModelMapper;
	
	/**
	 * 获取所有模板
	 * @return
	 */
	public List<WbsModel> queryWbsModelList(){
		return wbsModelMapper.queryModelList(TenantContext.getTenant());
	}
	
	/**
	 * 根据父节点id获取孩子记录
	 * @param id
	 * @return
	 */
	public List<ProjectCostInfo> queryWbsChildByParentId(String id){
		return wbsModelMapper.queryWbsChildByParentId(id,TenantContext.getTenant());
	}
	
	/**
	 * 根根据模板ID获取所有子节点
	 * @param id
	 * @return
	 */
//	public List<WbsModel> selectWbsModelNodeByModelId(String id){
//		return wbsModelMapper.selectWbsModelNodeByModelId(id,TenantContext.getTenant());
//	}
	
	
	 /**
     * 根据模板id查询子节点
     *
     * @param nodeList
     * @return
     */
    public List<WbsModel> getChilNode(String modelId) {
    	
//    	QueryWrapper<WbsModel> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("parent_id", modelId);
//		List<WbsModel> wbsModelList = this.list(queryWrapper);
//		
//        for (WbsModel wbsModel : wbsModelList) {
//        	nodeList.add(wbsModel);
//        	//递归,获取子节点
//        	this.getChilNode(nodeList,wbsModel.getId());
//        }
//        return nodeList;
    	
    	QueryWrapper<WbsModel> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", modelId);
		List<WbsModel> wbsModelList = this.list(queryWrapper);
		
        return wbsModelList;
        
    }


}
