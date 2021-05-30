package com.dongxin.erp.bm.service;

import com.dongxin.erp.bm.entity.BiddingInf;
import com.dongxin.erp.bm.entity.PurchasePlan;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dongxin.erp.bd.entity.AttachedUrl;
import com.dongxin.erp.bd.mapper.AttachedUrlMapper;
import com.dongxin.erp.bd.service.AttachedUrlService;
import com.dongxin.erp.bm.entity.BiddingDtl;
import com.dongxin.erp.bm.entity.BiddingEnterprise;
import com.dongxin.erp.bm.mapper.BiddingDtlMapper;
import com.dongxin.erp.bm.mapper.BiddingEnterpriseMapper;
import com.dongxin.erp.bm.mapper.BiddingInfMapper;
import com.dongxin.erp.bm.vo.BiddingInfPage;
import com.dongxin.erp.chart.ChartUtils;
import com.dongxin.erp.chart.mapper.BmChartMapper;
import com.dongxin.erp.chart.vo.Chart;
import com.dongxin.erp.chart.vo.MultiBar;
import com.dongxin.erp.enums.BiddingFlag;
import com.dongxin.erp.enums.BiddingStatus;
import com.dongxin.erp.exception.ErpException;

import io.netty.util.internal.StringUtil;

import org.jeecg.config.mybatis.TenantContext;
import org.springframework.stereotype.Service;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.base.service.BaseService;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: 招标信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Service
public class BiddingInfService extends BaseService<BiddingInfMapper, BiddingInf> {

	@Autowired
	private BiddingInfMapper biddingInfMapper;
	@Autowired
	private BiddingDtlMapper biddingDtlMapper;
	@Autowired
	private BiddingEnterpriseMapper biddingEnterpriseMapper;
	@Autowired
	private BiddingInfService biddingInfService;
	@Autowired
	private BmChartMapper chartMapper;

	@Autowired
	private BiddingDtlService biddingDtlService;

	@Autowired
	private PurchasePlanService purchasePlanService;

	@Autowired
	private BiddingEnterpriseService biddingEnterpriseService;

	@Autowired
	private AttachedUrlMapper attachedUrlMapper;

	@Autowired
	private AttachedUrlService attachedUrlService;

	@Transactional
	public void saveMain(BiddingInf biddingInf, List<BiddingDtl> biddingDtlList,
			List<BiddingEnterprise> biddingEnterpriseList) {
		biddingInfMapper.insert(biddingInf);
		if (biddingDtlList != null && biddingDtlList.size() > 0) {
			for (BiddingDtl entity : biddingDtlList) {
				// 外键设置
				entity.setBiddingInfId(biddingInf.getId());
				biddingDtlMapper.insert(entity);
			}
		}
		if (biddingEnterpriseList != null && biddingEnterpriseList.size() > 0) {
			for (BiddingEnterprise entity : biddingEnterpriseList) {
				// 外键设置
				entity.setBiddingInfId(biddingInf.getId());
				biddingEnterpriseMapper.insert(entity);
			}
		}
	}

	@Transactional
	public void updateMain(BiddingInf biddingInf, List<BiddingDtl> biddingDtlList,
			List<BiddingEnterprise> biddingEnterpriseList) {
		biddingInfMapper.updateById(biddingInf);

		// 1.先删除子表数据
		biddingDtlMapper.deleteByMainId(biddingInf.getId());
		biddingEnterpriseMapper.deleteByMainId(biddingInf.getId());

		// 2.子表数据重新插入
		if (biddingDtlList != null && biddingDtlList.size() > 0) {
			for (BiddingDtl entity : biddingDtlList) {
				// 外键设置
				entity.setBiddingInfId(biddingInf.getId());
				biddingDtlMapper.insert(entity);
			}
		}
		if (biddingEnterpriseList != null && biddingEnterpriseList.size() > 0) {
			for (BiddingEnterprise entity : biddingEnterpriseList) {
				// 外键设置
				entity.setBiddingInfId(biddingInf.getId());
				biddingEnterpriseMapper.insert(entity);
			}
		}
	}

	/**
	 * 删除招标信息
	 * 
	 * @param id
	 */
	@Transactional
	public void delete(String id) {
		// 删除招标信息
		this.removeById(id);

		// 删除招标明细信息
		biddingDtlService.updateStatusByBiddingInfId(id);

		// 删除招标采购计划信息
		purchasePlanService.updateStatusByBiddingInfId(id);

		// 将招标信息下所有企业置为删除状态
		biddingEnterpriseService.deleteByBiddingInfId(id);
		
		//将招标信息下所有附件信息置为删除状态
		attachedUrlService.deleteByRelationId(id);

	}
   
	/**
	 * 批量删除
	 * @param idList
	 */
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for (Serializable id : idList) {
//			biddingDtlMapper.deleteByMainId(id.toString());
//			biddingEnterpriseMapper.deleteByMainId(id.toString());
//			biddingInfMapper.deleteById(id);
			
			// 删除招标信息
			this.removeById(id);
			// 删除招标明细信息
			biddingDtlService.updateStatusByBiddingInfId(id.toString());
			// 删除招标采购计划信息
			purchasePlanService.updateStatusByBiddingInfId(id.toString());
			// 将招标信息下所有企业置为删除状态
			biddingEnterpriseService.deleteByBiddingInfId(id.toString());
			//将招标信息下所有附件信息置为删除状态
			attachedUrlService.deleteByRelationId(id.toString());
		}
	}

	public String selectUser(String id) {

		return biddingInfMapper.selectUser(id);
	}

	/*
	 * public List<BiddingInf> listBiddingInf(BiddingInf biddingInf) {
	 *//*
		 * if(!(biddingInf.getBiddingCategory()==null &&
		 * biddingInf.getOpenBiddingDate()==null && biddingInf.getEndBiddingDate()==null
		 * && biddingInf.getBiddingDepaterment()==null)) {
		 *//*
			 * String biddingTitle =biddingInf.getBiddingTitle(); String biddingDepaterment
			 * =biddingInf.getBiddingDepaterment(); return
			 * biddingInfMapper.selectBiddingInf(biddingTitle,biddingDepaterment) ; }
			 */
	// 模糊查询 范围查询
	public List<BiddingInf> biddingQuery(BiddingInf biddingInf, Date date, Date date2, List<BiddingInf> biddingInfs) {

		if (biddingInf.getEndTime() != null || biddingInf.getBeginTime() != null || date2 != null || date != null
				|| biddingInf.getBiddingTitle() != null || biddingInf.getBiddingDepaterment() != null) {
			return biddingInfMapper.biddingQuery(biddingInf.getBeginTime(), biddingInf.getEndTime(), date, date2,
					biddingInf.getBiddingTitle(), biddingInf.getBiddingDepaterment(), TenantContext.getTenant());
		}
		return biddingInfs;
	}

	public MultiBar listBiddingPrice(String id) {
		List<Chart> chartList = chartMapper.getMultiBarBiddingPrice(id, TenantContext.getTenant());
		return ChartUtils.getMultiBar(chartList);
	}

	/**
	 * 招标信息编辑保存
	 *
	 * @author huangheng
	 * @date 2020-12-23
	 * @param biddingInfPage
	 */
	@Transactional
	public void edit(BiddingInfPage biddingInfPage) {

		BiddingInf biddingInf = new BiddingInf();

		BeanUtils.copyProperties(biddingInfPage, biddingInf);
		String biddingInfId = biddingInf.getId();
		BiddingInf biddingInfEntity = this.getById(biddingInfId);
		if (biddingInfEntity == null) {
			throw new ErpException("未找到对应数据");
		}
		// 更新招标信息表
		biddingInfMapper.updateById(biddingInf);

		// 更新招标明细信息:先将原有所有记录置为删除状态,如果记录还存在,则更新回来;不存在则新增
		biddingDtlService.updateStatusByBiddingInfId(biddingInfId);
		purchasePlanService.updateStatusByBiddingInfId(biddingInfId);

		List<BiddingDtl> biddingDtlList = biddingInfPage.getBiddingDtlList();
		for (BiddingDtl biddingDtl : biddingDtlList) {

			BiddingDtl biddingDtlEntity = biddingDtlService.getBiddingDtlById(biddingDtl.getId());
			if (null == biddingDtlEntity) {

				// 添加采购计划明细记录
				PurchasePlan purchasePlan = new PurchasePlan();
				purchasePlan.setPurchasePlanId(biddingDtl.getId());// 采购申请明细号

				String biddingDetailId = null;
				// 先根据当前招投标号+物料编号查询招标明细信息表，如果存在则更新重量;否则新增
				BiddingDtl biddingDtlBean = biddingDtlMapper.selectOneByBiddingInfIdAndMaterielNo(biddingInfId,
						biddingDtl.getMaterielNo(), TenantContext.getTenant());
				if (null == biddingDtlBean) {
					biddingDtl.setBiddingInfId(biddingInf.getId());
					biddingDtlService.save(biddingDtl);
					biddingDetailId = biddingDtl.getId();
				} else {
					biddingDtlBean.setMeasureNum(biddingDtl.getMeasureNum() + biddingDtlBean.getMeasureNum());
					biddingDtlService.updateById(biddingDtlBean);
					biddingDetailId = biddingDtlBean.getId();
				}

				purchasePlan.setBiddingDetailId(biddingDetailId);// 招投标明细号
				purchasePlan.setBiddingInfId(biddingInfId);
				purchasePlan.setMaterielNo(biddingDtl.getMaterielNo());
				purchasePlan.setMaterielName(biddingDtl.getMaterielName());
				purchasePlan.setMeasureUnit(biddingDtl.getMeasureUnit());
				purchasePlan.setMeasureNum(biddingDtl.getMeasureNum());
				purchasePlan.setRemark(biddingDtl.getRemark());
				purchasePlanService.save(purchasePlan);

			} else {

				// 先根据当前招投标号+物料编号查询招标明细信息表，如果存在则更新重量;否则更新为有效
				BiddingDtl biddingDtlBean = biddingDtlMapper.selectOneByBiddingInfIdAndMaterielNo(
						biddingDtlEntity.getBiddingInfId(), biddingDtlEntity.getMaterielNo(),
						TenantContext.getTenant());
				if (null == biddingDtlBean) {
					// 更新招标明细记录状态为0-有效
					biddingDtlService.updateDelFlagById(biddingDtl.getId());
					// 对应的采购计划明细记录状态更新为0-有效
					purchasePlanService.updateDelFlagByBiddingDetailId(biddingDtl.getId());
				} else {
					biddingDtlBean.setMeasureNum(biddingDtlEntity.getMeasureNum() + biddingDtlBean.getMeasureNum());
					biddingDtlService.updateById(biddingDtlBean);

					// 对应的采购计划明细记录状态更新为0-有效
					purchasePlanService.updateDelFlagByBiddingDetailId(biddingDtl.getId());

					// 采购明细记录划入新的招标明细下
					QueryWrapper<PurchasePlan> queryWrapper = new QueryWrapper<>();
					queryWrapper.eq("bidding_detail_id", biddingDtl.getId());
					List<PurchasePlan> purchasePlans = purchasePlanService.list(queryWrapper);
					for (PurchasePlan purchasePlan : purchasePlans) {
						purchasePlan.setBiddingDetailId(biddingDtlBean.getId());
						purchasePlanService.updateById(purchasePlan);
					}
				}
			}
		}

		// 更新招标企业信息

		// 将招标信息下所有企业置为删除状态
		biddingEnterpriseService.deleteByBiddingInfId(biddingInfId);

		List<BiddingEnterprise> biddingEnterpriseList = biddingInfPage.getBiddingEnterpriseList();
		for (BiddingEnterprise biddingEnterprise : biddingEnterpriseList) {
			if (null == biddingEnterprise.getEnterpriceNo()) {
				throw new ErpException("企业编码不能为空");
			}

			// 根据ID查找是否已存在记录,存在则置为有效；否则新增
			BiddingEnterprise biddingEnterpriseEntity = biddingEnterpriseService
					.getBiddingEnterpriseById(biddingEnterprise.getId());

			if (null == biddingEnterpriseEntity) {
				biddingEnterprise.setBiddingInfId(biddingInfId);
				biddingEnterpriseMapper.insert(biddingEnterprise);
			} else {
				// 更新状态为0-有效
				biddingEnterpriseService.updateDelFlagById(biddingEnterpriseEntity.getId());
			}
		}

		// 更新附件信息
		attachedUrlService.deleteByRelationId(biddingInfId);
		
		String fileUrl = biddingInfPage.getUrl();
		if (!StringUtil.isNullOrEmpty(fileUrl)) {
			if (fileUrl.contains(",")) {
				String[] url = fileUrl.split(",");
				for (int index = 0; index < url.length; index++) {

					AttachedUrl attachedUrlEntity = attachedUrlService.selectOne(biddingInfId, url[index], index + 1);
					if (null == attachedUrlEntity) {
						AttachedUrl attachedUrl = new AttachedUrl();
						attachedUrl.setRelationId(biddingInfId);
						attachedUrl.setUrl(url[index]);
						attachedUrl.setSort(index + 1);
						attachedUrlMapper.insert(attachedUrl);
					} else {
						// 更新状态为0-有效
						attachedUrlService.updateDelFlagById(attachedUrlEntity.getId());
					}
				}
			} else {
				AttachedUrl attachedUrlEntity = attachedUrlService.selectOne(biddingInfId, fileUrl, 1);
				if (null == attachedUrlEntity) {
					AttachedUrl attachedUrl = new AttachedUrl();
					attachedUrl.setRelationId(biddingInfId);
					attachedUrl.setUrl(fileUrl);
					attachedUrl.setSort(1);
					attachedUrlMapper.insert(attachedUrl);
				} else {
					// 更新状态为0-有效
					attachedUrlService.updateDelFlagById(attachedUrlEntity.getId());
				}
			}
		}
	}

	/**
	 * 招标信息新增
	 * 
	 * @author huangheng 2020-12-25
	 * @param biddingInfPage
	 */
	@Transactional
	public void add(BiddingInfPage biddingInfPage) {
		BiddingInf biddingInf = new BiddingInf();

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 获取当前操作人
		String userName = loginUser.getRealname();
		// 获取当前操作人的单位名称
		String departId = loginUser.getId();
		String departName = biddingInfService.selectUser(departId);
		if (biddingInfPage.getBiddingMan() == null) {
			biddingInfPage.setBiddingMan(userName);
		}
		if (biddingInfPage.getBiddingDepaterment() == null) {
			biddingInfPage.setBiddingDepaterment(departName);
		}
		// 招标状态判断
		for (int i = 0; i < biddingInfPage.getBiddingEnterpriseList().size(); i++) {
			String a = biddingInfPage.getBiddingEnterpriseList().get(i).getBiddingStatus();
			if (a.equals(BiddingFlag.BIDWIN.getCode())) {
				break;
			} else {
				Date date = new Date();
				Date b = biddingInfPage.getEndBiddingDate();
				Date c = biddingInfPage.getOpenBiddingDate();
				if (b != null && date.after(b)) {
					biddingInfPage.setBiddingFlag(BiddingStatus.BIDeNDING.getCode());
					break;
				} else if (c != null && date.after(c)) {
					biddingInfPage.setBiddingFlag(BiddingStatus.BIDOPENING.getCode());
					break;
				} else {
					biddingInfPage.setBiddingFlag(BiddingStatus.BIDPENDING.getCode());
				}
			}
		}

		BeanUtils.copyProperties(biddingInfPage, biddingInf);
		// 招标信息新增
		biddingInfMapper.insert(biddingInf);

		String biddingInfId = biddingInf.getId();

		// 招标明细新增
		List<BiddingDtl> biddingDtls = biddingInfPage.getBiddingDtlList();

		// 招标明细信息列表
		List<BiddingDtl> biddingDtlList = new ArrayList<BiddingDtl>();
		// 招标采购计划明细列表
		List<PurchasePlan> purchasePlanList = new ArrayList<PurchasePlan>();

		// 封装招标明细信息和招标采购计划明细
		for (BiddingDtl dtl : biddingDtls) {
			PurchasePlan tbmPurchasePlan = new PurchasePlan();
			tbmPurchasePlan.setPurchasePlanId(dtl.getId());// 采购申请明细号
			tbmPurchasePlan.setBiddingInfId(dtl.getBiddingInfId());
			tbmPurchasePlan.setBiddingInfId(biddingInfId);
			tbmPurchasePlan.setMaterielNo(dtl.getMaterielNo());
			tbmPurchasePlan.setMaterielName(dtl.getMaterielName());
			tbmPurchasePlan.setMeasureUnit(dtl.getMeasureUnit());
			tbmPurchasePlan.setMeasureNum(dtl.getMeasureNum());
			tbmPurchasePlan.setRemark(dtl.getRemark());
			purchasePlanList.add(tbmPurchasePlan);

			if (biddingDtlList.size() == 0) {
				dtl.setBiddingInfId(biddingInfId);
				biddingDtlList.add(dtl);
			} else {
				Boolean if_exist = false;
				for (BiddingDtl biddingDetail : biddingDtlList) {
					// 物料号相同进行汇总
					if (dtl.getMaterielNo().equals(biddingDetail.getMaterielNo())) {
						biddingDetail.setMeasureNum(biddingDetail.getMeasureNum() + dtl.getMeasureNum());
						if_exist = true;
						break;
					}
				}
				if (if_exist == false) {
					dtl.setBiddingInfId(biddingInfId);
					biddingDtlList.add(dtl);
				}
			}
		}
		// 保存数据
		for (BiddingDtl biddingDetail : biddingDtlList) {
			biddingDetail.setId(null);
			// 招标明细添加
			biddingDtlService.save(biddingDetail);
			String biddingDetailId = biddingDetail.getId();
			// 招标采购计划明细添加
			for (PurchasePlan purchasePlan : purchasePlanList) {
				if (biddingDetail.getMaterielNo().equals(purchasePlan.getMaterielNo())) {
					purchasePlan.setBiddingDetailId(biddingDetailId);
					purchasePlanService.save(purchasePlan);
				}
			}
		}
		// 招标企业信息新增
		List<BiddingEnterprise> biddingEnterpriseList = biddingInfPage.getBiddingEnterpriseList();
		if (biddingEnterpriseList != null && biddingEnterpriseList.size() > 0) {
			for (BiddingEnterprise entity : biddingEnterpriseList) {
				// 外键设置
				entity.setBiddingInfId(biddingInfId);
				biddingEnterpriseMapper.insert(entity);
			}
		}

		// 附件新增
		String fileUrl = biddingInfPage.getUrl();
		if (!StringUtil.isNullOrEmpty(fileUrl)) {
			if (fileUrl.contains(",")) {
				String[] url = fileUrl.split(",");
				for (int index = 0; index < url.length; index++) {
					AttachedUrl attachedUrl = new AttachedUrl();
					attachedUrl.setRelationId(biddingInfId);
					attachedUrl.setUrl(url[index]);
					attachedUrl.setSort(index + 1);
					attachedUrlMapper.insert(attachedUrl);
				}
			} else {
				AttachedUrl attachedUrl = new AttachedUrl();
				attachedUrl.setRelationId(biddingInfId);
				attachedUrl.setUrl(fileUrl);
				attachedUrl.setSort(1);
				attachedUrlMapper.insert(attachedUrl);
			}
		}

	}

}
