package com.dongxin.erp.sm.service;

import com.dongxin.erp.sm.entity.MatlMoveOrderDtl;
import com.dongxin.erp.sm.entity.MatlMoveOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dongxin.erp.sm.entity.MatlOutOrderDtl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 移库单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface IMatlMoveOrderService extends IService<MatlMoveOrder> {

	public int myDelBatchMain(Collection<? extends Serializable> idList);
	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(MatlMoveOrder matlMoveOrder, List<MatlMoveOrderDtl> matlMoveOrderDtlList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(MatlMoveOrder matlMoveOrder, List<MatlMoveOrderDtl> matlMoveOrderDtlList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);

	public String checks(ArrayList<MatlMoveOrder> matlMoveOrders);


	void redFlushByIds(List<MatlMoveOrderDtl> listDtl);

	void redFlushById(String id);
}
