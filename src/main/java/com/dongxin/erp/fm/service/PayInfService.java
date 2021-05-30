package com.dongxin.erp.fm.service;

import cn.hutool.core.map.MapUtil;
import com.dongxin.erp.enums.Status;
import com.dongxin.erp.fm.entity.PayInf;
import com.dongxin.erp.fm.entity.PayDtl;
import com.dongxin.erp.fm.mapper.PayDtlMapper;
import com.dongxin.erp.fm.mapper.PayInfMapper;

import com.dongxin.erp.fm.vo.PayInfPage;
import com.dongxin.erp.mm.service.ContractDtlService;
import com.dongxin.erp.mm.service.ContractService;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.config.mybatis.TenantContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 财务付款信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class PayInfService extends BaseService<PayInfMapper, PayInf>{

	@Autowired
	private PayInfMapper payInfMapper;
	@Autowired
	private PayDtlMapper payDtlMapper;
	@Autowired
	private PayInfService payInfService;
   @Autowired
   ContractDtlService contractDtlService;

	@Transactional
	public void saveMain(PayInf payInf, List<PayDtl> payDtlList) {
		payInfMapper.insert(payInf);
		if(payDtlList!=null && payDtlList.size()>0) {
			for(PayDtl entity:payDtlList) {
				//外键设置
				entity.setPayId(payInf.getId());
				payDtlMapper.insert(entity);
			}
		}
	}


	@Transactional
	public void updateMain(PayInf payInf,List<PayDtl> payDtlList) {
		payInfMapper.updateById(payInf);
		
		//1.先删除子表数据
		payDtlMapper.deleteByMainId(payInf.getId());
		
		//2.子表数据重新插入
		if(payDtlList!=null && payDtlList.size()>0) {
			for(PayDtl entity:payDtlList) {
				//外键设置
				entity.setPayId(payInf.getId());
				payDtlMapper.insert(entity);
			}
		}
	}

	@Transactional
	public void delMain(String id) {
		payDtlMapper.deleteByMainId(id);
		payInfMapper.deleteById(id);
	}


	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			payDtlMapper.deleteByMainId(id.toString());
			payInfMapper.deleteById(id);
		}
	}

	@Transactional
	public void check(ArrayList<PayInf> payInfs){
		Date date = new Date();
		ArrayList<PayInf> list = new ArrayList<>();
		for (PayInf payInf:payInfs) {
			payInf.setAppStatus(Status.CHECK.getCode());
			payInf.setAppDate(date);
			list.add(payInf);
		}
		payInfService.saveOrUpdateBatch(list);

	}

 public List<PayInf> payInfQuery(PayInf payInf, List<PayInf> payInfs){
		if(payInf.getCode()!=null ||payInf.getPayNatureName()!=null ||payInf.getContractCode()!=null ||
				payInf.getCsNameRecept()!=null ||payInf.getBeginTime()!=null ||payInf.getEndTime()!=null){
               return payInfMapper.payInfQuery(payInf.getCode(),payInf.getPayNatureName(),payInf.getContractCode(),payInf.getCsNameRecept(),payInf.getBeginTime(),payInf.getEndTime(),TenantContext.getTenant());
		}
		return payInfs;

	}


	public Map<String, String> selectPayNature() {
		Map <String,String> idAndNameOfMaps = new HashMap<>();
		List<Map<String, String>> maps = payInfMapper.selectPayNature();
		for (Map map : maps){
			idAndNameOfMaps.put(MapUtil.getStr(map,"id"),MapUtil.getStr(map,"name"));
		}
		return idAndNameOfMaps;
	}


	public boolean add(PayInfPage payInfPage) {
		/*PayInf payInf = new PayInf();*/
		String id=payInfPage.getContractId();
		BigDecimal price=contractDtlService.selectprice(id);
		//计算付款总金额
		BigDecimal sum=new BigDecimal(0);
		for (int i = 0; i < payInfPage.getPayDtlList().size(); i++) {
			sum = sum.add(payInfPage.getPayDtlList().get(i).getMoney());
		}
			if(price.compareTo(sum) > -1) {
				payInfPage.setTotalSum(sum);

			}
		boolean b=price.compareTo(sum) > -1;
		return b;
	}
}
