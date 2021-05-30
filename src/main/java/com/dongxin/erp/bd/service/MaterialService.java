package com.dongxin.erp.bd.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.bd.entity.Material;
import com.dongxin.erp.bd.mapper.MaterialMapper;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.jeecg.common.system.base.service.BaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 物料信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class MaterialService extends BaseService<MaterialMapper, Material>{
    @Autowired
	MaterialMapper materialMapper;
    /**根节点父ID的值*/
    public static final String ROOT_PID_VALUE = "0";

    /**树节点有子节点状态值*/
    public static final String HASCHILD = "1";

    /**树节点无子节点状态值*/
    public static final String NOCHILD = "0";
 //显示计量单位名称
    public  Map<String, String> selectUnit() {
		Map<String,String> idAndNameOfMaps=new HashMap<>();
		List<Map<String,String>> maps=this.materialMapper.selectUnit(TenantContext.getTenant());
		for(Map map:maps){
			idAndNameOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"name"));
		}
		return idAndNameOfMaps;
	}


    public void addMaterial(Material material) {
		if(oConvertUtils.isEmpty(material.getPid())){
			material.setPid(ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			Material parent = baseMapper.selectById(material.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(material);
	}

	public void updateMaterial(Material material) {
		Material entity = this.getById(material.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = material.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				material.setPid(ROOT_PID_VALUE);
			}
			if(!ROOT_PID_VALUE.equals(material.getPid())) {
				baseMapper.updateTreeNodeStatus(material.getPid(), HASCHILD);
			}
		}
		baseMapper.updateById(material);
	}

	public void deleteMaterial(String id) throws JeecgBootException {
		Material material = this.getById(id);
		if(material==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(material.getPid());
		logicDeleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<Material>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, NOCHILD);
			}
		}
	}

	public String idAndName(String id){
		Material material = materialMapper.selectById(id);
		if (material != null){
			return material.getName();
		}
		return null;
	}


	public List<Material> selectMaterialId(String orgId) {
		List<Material> materials=materialMapper.selectMaterialId(orgId);
		return materials;
	}
}
