package com.dongxin.erp.bd.mapper;

import org.apache.ibatis.annotations.Param;
import com.dongxin.erp.bd.entity.Node;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Description: 库存地信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Component
public interface NodeMapper extends BaseMapper<Node> {

	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id, @Param("status") String status);

}
