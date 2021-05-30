package com.dongxin.erp.mm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.mm.entity.Plan;
import com.dongxin.erp.mm.entity.PlanDtl;
import com.dongxin.erp.mm.mapper.PlanDtlMapper;
import com.dongxin.erp.mm.mapper.PlanMapper;
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
 * @Description: 采购申请主表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class PlanService extends BaseService<PlanMapper, Plan> {

	@Autowired
	private PlanMapper planMapper;
	@Autowired
	private PlanDtlMapper planDtlMapper;

	@Autowired
    PlanService planService;

	@Autowired
	PlanDtlService planDtlService;

	@Transactional
	public void saveMain(Plan plan, List<PlanDtl> planDtlList) {
		planMapper.insert(plan);
		if(planDtlList!=null && planDtlList.size()>0) {
			for(PlanDtl entity:planDtlList) {
//				//短文本校验
//				if(entity.getTbdMaterialId()!=null){
//					entity.setShortText(entity.getTbdMaterialId());
//				}
				//外键设置
				entity.setTmmPlanId(plan.getId());
				planDtlMapper.insert(entity);
			}
		}
	}

	@Transactional
	public void updateMain(Plan plan,List<PlanDtl> planDtlList) {
		List<String> receiveIds = new ArrayList<>();
		for (PlanDtl planDtl : planDtlList) {
			planDtl.setTmmPlanId(plan.getId());
			receiveIds.add(planDtl.getId());
		}
		planDtlService.saveOrUpdateBatch(planDtlList);
		QueryWrapper<PlanDtl> wrapper = new QueryWrapper<>();
		wrapper.eq("tmm_plan_id", plan.getId());
		List<PlanDtl> list = planDtlService.list(wrapper);
		updateById(plan);
		//如果相等, 则说明没有要删除的记录
		if (list.size() == planDtlList.size()) {
			return;
		} else {
			//removeIds: 要删除的记录ids
			List<String> removeIds = new ArrayList<>();
			for (PlanDtl planDtl : list) {
				removeIds.add(planDtl.getId());
			}
			removeIds.removeAll(receiveIds);
			planDtlMapper.deleteBatchIds(removeIds);
		}

//
//		//1.先删除子表数据
//		planDtlMapper.deleteByMainId(plan.getId());
//
//		//2.子表数据重新插入
//		if(planDtlList!=null && planDtlList.size()>0) {
//			for(PlanDtl entity:planDtlList) {
//				//外键设置
//				entity.setTmmPlanId(plan.getId());
//				planDtlMapper.insert(entity);
//			}
//		}
	}

	@Transactional
	public String delMain(String id) {
		planDtlMapper.deleteByMainId(id);
		int i = planMapper.deleteById(id);
		if (i == 0) {
			return "请选择未审核的记录删除";
		}
		return "已删除" + i + "条记录";
	}

	@Transactional
	public int delBatchMain(Collection<? extends Serializable> idList) {
		int i = planMapper.deleteBatchIds(idList);
		QueryWrapper<PlanDtl> wrapper = new QueryWrapper<>();
		wrapper.in("tmm_plan_id",idList);
		planDtlMapper.delete(wrapper);
		return i;
	}

	@Transactional
	public void check(ArrayList<Plan> plans) {
        Date date = new Date();
        ArrayList<Plan> list = new ArrayList<>();
        for (Plan plan : plans) {
            plan.setStatus(Status.CHECK.getCode());
            plan.setVoucherTime(date);
            list.add(plan);
        }
         planService.saveOrUpdateBatch(list);

    }
}
