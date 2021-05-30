package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlInOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 收货单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-11
 * @Version: V1.0
 */
public interface IMatlInOrderService extends IService<MatlInOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(MatlInOrder matlInOrder,List<MatlInOrderDtl> matlInOrderDtlList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(MatlInOrder matlInOrder,List<MatlInOrderDtl> matlInOrderDtlList);
	
	/**
	 * 删除一对多
	 * @return
	 */
	public String delMain (String id);
	
	/**
	 * 批量删除一对多
     * @return
     */
	public int delBatchMain (Collection<? extends Serializable> idList);

    void redFlushById(String ids);

	void redFlushByIds(List<MatlInOrderDtl> matlInOrderDtlList);
}
