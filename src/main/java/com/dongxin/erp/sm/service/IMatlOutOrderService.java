package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlInOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;
import com.dongxin.erp.sm.entity.MatlOutOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 领用单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface IMatlOutOrderService extends IService<MatlOutOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(MatlOutOrder matlOutOrder, List<MatlOutOrderDtl> matlOutOrderDtlList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(MatlOutOrder matlOutOrder, List<MatlOutOrderDtl> matlOutOrderDtlList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);

	/**
	 * 批量删除一对多
	 * @return
	 */
	public int delBatchMain(Collection<? extends Serializable> idList);

    public String checks(ArrayList<MatlOutOrder> matlOutOrders);


	void redFlushByIds(List<MatlOutOrderDtl> listDtl);

	void redFlushById(String id);
}
