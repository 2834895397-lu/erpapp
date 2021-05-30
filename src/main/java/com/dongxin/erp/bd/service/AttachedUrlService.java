package com.dongxin.erp.bd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.dongxin.erp.bd.entity.AttachedUrl;
import com.dongxin.erp.bd.mapper.AttachedUrlMapper;
import com.dongxin.erp.bm.entity.BiddingEnterprise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;

/**
 * @Description: 附件地址表
 * @Author: huangheng
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Service
public class AttachedUrlService extends BaseService<AttachedUrlMapper, AttachedUrl> {

	@Autowired
	private AttachedUrlMapper attachedUrlMapper;
	/**
	 * 通过关联主键查询附件地址
	 * @param relationId
	 * @return
	 */
	public String getAttachedUrlByRelationId(String relationId) {
		QueryWrapper<AttachedUrl> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("relation_id", relationId);
		queryWrapper.orderByAsc("sort");
		List<AttachedUrl> urlList = this.list(queryWrapper);
		if(urlList.size()>0) {
			if(urlList.size() == 1 ) {
				return urlList.get(0).getUrl();
			}else {
				List<String> list = new ArrayList<>();
				for(int index=0;index<urlList.size();index++) {
					list.add(urlList.get(index).getUrl());
				}
				return StringUtils.join(list, ",");
			}
		}else {
			return null;
		}
	}
	
	/**
	 * 根据关联主键ID删除
	 * @param relationId
	 */
	public void deleteByRelationId(String relationId){
		QueryWrapper<AttachedUrl> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("relation_id", relationId);
		List<AttachedUrl> attachedUrls = this.list(queryWrapper);
		for (AttachedUrl attachedUrl : attachedUrls) {
			this.logicDelete(attachedUrl);
		}
	}
	
	/**
	 * 获取一条附件地址记录
	 * @param relationId
	 * @param url
	 * @param sort
	 * @return
	 */
	public AttachedUrl selectOne(String relationId,String url,int sort) {
		return attachedUrlMapper.selectOne(relationId,url,sort,TenantContext.getTenant());
	}
	
	/**
	 * 更新状态为有效
	 * @param id
	 */
	public void updateDelFlagById(String id) {
		attachedUrlMapper.updateDelFlagById(id,TenantContext.getTenant());
	}
}
