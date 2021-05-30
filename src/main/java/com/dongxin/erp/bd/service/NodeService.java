package com.dongxin.erp.bd.service;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.bd.entity.Node;
import com.dongxin.erp.bd.mapper.NodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.jeecg.common.system.base.service.BaseService;

/**
 * @Description: 库存地信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Service
public class NodeService extends BaseService<NodeMapper, Node>{

	@Autowired
	NodeMapper nodeMapper;
    /**根节点父ID的值*/
    public static final String ROOT_PID_VALUE = "0";

    /**树节点有子节点状态值*/
    public static final String HASCHILD = "1";

    /**树节点无子节点状态值*/
    public static final String NOCHILD = "0";

	public void addNode(Node node) {
		if(oConvertUtils.isEmpty(node.getPid())){
			node.setPid(ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			Node parent = baseMapper.selectById(node.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(node);
	}

	public void updateNode(Node node) {
		Node entity = this.getById(node.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = node.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				node.setPid(ROOT_PID_VALUE);
			}
			if(!ROOT_PID_VALUE.equals(node.getPid())) {
				baseMapper.updateTreeNodeStatus(node.getPid(), HASCHILD);
			}
		}
		baseMapper.updateById(node);
	}

	public void deleteNode(String id) throws JeecgBootException {
		Node node = this.getById(id);
		if(node==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		updateOldParentNode(node.getPid());
		logicDeleteById(id);
	}
	
	
	
	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!ROOT_PID_VALUE.equals(pid)) {
			Integer count = baseMapper.selectCount(new QueryWrapper<Node>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, NOCHILD);
			}
		}
	}

	public String idAndName(String id){
		Node node = nodeMapper.selectById(id);
		if(node != null){
			return node.getName();
		}
		return null;
	}

}
