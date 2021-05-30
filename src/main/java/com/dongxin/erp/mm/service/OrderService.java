package com.dongxin.erp.mm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.mm.entity.Order;
import com.dongxin.erp.mm.entity.OrderDtl;
import com.dongxin.erp.mm.mapper.OrderDtlMapper;
import com.dongxin.erp.mm.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购订单主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class OrderService extends BaseService<OrderMapper, Order> {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDtlMapper orderDtlMapper;
	@Autowired
	OrderService orderService;

	@Autowired
	OrderDtlService orderDtlService;


	@Transactional
	public void saveMain(Order order, List<OrderDtl> orderDtlList) {
		orderMapper.insert(order);
		if(orderDtlList!=null && orderDtlList.size()>0) {
			for(OrderDtl entity:orderDtlList) {
//				//短文本校验
//				if(entity.getTbdMaterialId()!=null){
//					entity.setShortText(entity.getTbdMaterialId());
//				}
				//外键设置
				entity.setTmmPurchaseOrderId(order.getId());
				orderDtlMapper.insert(entity);
			}
		}
	}

	@Transactional
	public void updateMain(Order order,List<OrderDtl> orderDtlList) {
		List<String> receiveIds = new ArrayList<>();
		for (OrderDtl orderDtl : orderDtlList) {
			orderDtl.setTmmPurchaseOrderId(order.getId());
			receiveIds.add(orderDtl.getId());
		}
		orderDtlService.saveOrUpdateBatch(orderDtlList);
		QueryWrapper<OrderDtl> wrapper = new QueryWrapper<>();
		wrapper.eq("tmm_purchase_order_id", order.getId());
		List<OrderDtl> list = orderDtlService.list(wrapper);
		updateById(order);
		//如果相等, 则说明没有要删除的记录
		if (list.size() == orderDtlList.size()) {
			return;
		} else {
			//removeIds: 要删除的记录ids
			List<String> removeIds = new ArrayList<>();
			for (OrderDtl orderDtl : list) {
				removeIds.add(orderDtl.getId());
			}
			removeIds.removeAll(receiveIds);
			orderDtlMapper.deleteBatchIds(removeIds);
		}

//
//		//1.先删除子表数据
//		orderDtlMapper.deleteByMainId(order.getId());
//
//		//2.子表数据重新插入
//		if(orderDtlList!=null && orderDtlList.size()>0) {
//			for(OrderDtl entity:orderDtlList) {
//				//外键设置
//				entity.setTmmOrderId(order.getId());
//				orderDtlMapper.insert(entity);
//			}
//		}
	}

	@Transactional
	public String delMain(String id) {
		orderDtlMapper.deleteByMainId(id);
		int i = orderMapper.deleteById(id);
		if (i == 0) {
			return "请选择未审核的记录删除";
		}
		return "已删除" + i + "条记录";
	}

	@Transactional
	public int delBatchMain(Collection<? extends Serializable> idList) {
		int i = orderMapper.deleteBatchIds(idList);
		QueryWrapper<OrderDtl> wrapper = new QueryWrapper<>();
		wrapper.in("tmm_purchase_order_id",idList);
		orderDtlMapper.delete(wrapper);
		return i;
	}
	@Transactional
	public void check(ArrayList<Order> orders) {
		Date date = new Date();
		ArrayList<Order> list = new ArrayList<>();
		for (Order order : orders) {
			order.setStatus(Status.CHECK.getCode());
			order.setVoucherTime(date);
			list.add(order);
		}
		orderService.saveOrUpdateBatch(list);

	}
}
