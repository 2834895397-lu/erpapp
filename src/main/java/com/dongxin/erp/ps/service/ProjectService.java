package com.dongxin.erp.ps.service;

import com.dongxin.erp.bd.entity.AttachedUrl;
import com.dongxin.erp.bd.mapper.AttachedUrlMapper;
import com.dongxin.erp.bd.service.AttachedUrlService;
import com.dongxin.erp.cs.entity.ProfileInfo;
import com.dongxin.erp.cs.service.ProfileInfService;
import com.dongxin.erp.enums.TpsProjectStatus;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.ps.entity.Project;
import com.dongxin.erp.ps.entity.ProjectCost;
import com.dongxin.erp.ps.entity.ProjectCostInfo;
import com.dongxin.erp.ps.entity.ProjectDetail;
import com.dongxin.erp.ps.entity.ProjectSaveEntity;
import com.dongxin.erp.ps.entity.ProjectWbs;
import com.dongxin.erp.ps.entity.PsContract;
import com.dongxin.erp.ps.entity.WbsModel;

import io.netty.util.internal.StringUtil;

import com.dongxin.erp.ps.mapper.ProjectMapper;
import org.springframework.stereotype.Service;
import org.jeecg.common.system.base.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @Description: 项目基础信息表
 * @Author: jeecg-boot/huangheng
 * @Date: 2021-01-15
 * @Version: V1.0
 */
@Service
public class ProjectService extends BaseService<ProjectMapper, Project> {

	@Autowired
	private ProjectCostService projectCostService;

	@Autowired
	private AttachedUrlMapper attachedUrlMapper;

	@Autowired
	private PsContractService contractService;

	@Autowired
	private ProfileInfService profileInfService;

	@Autowired
	private AttachedUrlService attachedUrlService;

	@Autowired
	private ProjectWbsService projectWbsService;

	@Autowired
	private WbsModelService wbsModelService;

	/**
	 * 删除项目记录
	 * 
	 * @param id
	 */
	@Transactional
	public void delMain(String id) {
		// 1、删除费用明细表
		projectCostService.deleteBatchByMainId(id);

		// 2、删除附件信息表
		attachedUrlService.deleteByRelationId(id);

		// 3、删除主表
		logicDeleteById(id);

		// 4、删除项目wbs结构
		projectWbsService.deleteByProjectId(id);
	}

	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for (Serializable id : idList) {
			projectCostService.deleteBatchByMainId(id.toString());
			attachedUrlService.deleteByRelationId(id.toString());
			logicDeleteById(id);
			// 删除项目wbs结构
			projectWbsService.deleteByProjectId(id.toString());
		}
	}

	/**
	 * 项目信息保存
	 * 
	 * @param projectSaveEntity
	 */
	@Transactional
	public void add(ProjectSaveEntity projectSaveEntity) {

		Project project = new Project();
		BeanUtils.copyProperties(projectSaveEntity, project);

		// 增加临时合同
		if (null == projectSaveEntity.getContractId()) {
			PsContract contract = new PsContract();
			contract.setContractTitle(project.getProjectName());
			contract.setPayBb(projectSaveEntity.getPayBb());
			contract.setOperator(projectSaveEntity.getOperator());

			// 甲方公司信息
			ProfileInfo profileInfoA = profileInfService.getProfileInfoByMainId(projectSaveEntity.getPartyAId());
			if (null != profileInfoA) {
				contract.setPartyAId(profileInfoA.getId());
				contract.setPartyAName(profileInfoA.getCsName());
				contract.setPartyALegal(profileInfoA.getLegal());
				contract.setPartyATrusteeName(profileInfoA.getTrusteeName());
				contract.setPartyABankAccount(profileInfoA.getBankAccount());
				contract.setPartyABankName(profileInfoA.getOpeningBank());
			}

			// 乙方公司信息
			ProfileInfo profileInfoB = profileInfService.getProfileInfoByMainId(projectSaveEntity.getPartyBId());
			if (null != profileInfoB) {
				contract.setPartyBId(profileInfoB.getId());
				contract.setPartyBName(profileInfoB.getCsName());
				contract.setPartyBLegal(profileInfoB.getLegal());
				contract.setPartyBTrusteeName(profileInfoB.getTrusteeName());
				contract.setPartyBBankAccount(profileInfoB.getBankAccount());
				contract.setPartyBBankName(profileInfoB.getOpeningBank());
			}

			contractService.save(contract);// 保存临时合同

			project.setContractId(contract.getId());// 设置合同ID
		}
		project.setStatus(TpsProjectStatus.PENDING.getCode());
		this.save(project);
		String projectId = project.getId();

		// 新增费用信息
		List<ProjectCost> projectCostList = projectSaveEntity.getProjectCostList();
		for (ProjectCost projectCost : projectCostList) {
			projectCost.setId(null);
			projectCost.setProjectId(projectId);
			projectCostService.save(projectCost);
		}

		// 附件新增
		String fileUrl = projectSaveEntity.getUrl();
		if (!StringUtil.isNullOrEmpty(fileUrl)) {
			if (fileUrl.contains(",")) {
				String[] url = fileUrl.split(",");
				for (int index = 0; index < url.length; index++) {
					AttachedUrl attachedUrl = new AttachedUrl();
					attachedUrl.setRelationId(projectId);
					attachedUrl.setUrl(url[index]);
					attachedUrl.setSort(index + 1);
					attachedUrlMapper.insert(attachedUrl);
				}
			} else {
				AttachedUrl attachedUrl = new AttachedUrl();
				attachedUrl.setRelationId(projectId);
				attachedUrl.setUrl(fileUrl);
				attachedUrl.setSort(1);
				attachedUrlMapper.insert(attachedUrl);
			}
		}

		// 新增项目分解结构

		//List<ProjectWbs> projectWbsList = new ArrayList<ProjectWbs>();

		//模板ID
		String wbsModelId = projectSaveEntity.getWbsId();
		WbsModel wbsModel = wbsModelService.getById(wbsModelId);
		if (null != wbsModel) {
			ProjectWbs projectWbs = new ProjectWbs();
			BeanUtils.copyProperties(wbsModel, projectWbs);
			projectWbs.setId(null);
			projectWbs.setProjectId(projectId);
			projectWbs.setWbsId(wbsModelId);
			//projectWbsList.add(projectWbs);
			
			projectWbsService.save(projectWbs);
			
			this.saveProjectChildNode(wbsModel.getId(),projectWbs.getId(),projectId,wbsModelId);
			// 获取所有子节点
//			List<WbsModel> wbsModelList = new ArrayList<WbsModel>();
//			wbsModelList = wbsModelService.getChilNode(wbsModelList,wbsModel.getId());
//			if (wbsModelList.size() > 0) {
//				for (WbsModel wbsModelEntity : wbsModelList) {
//					ProjectWbs projectWbsEntity = new ProjectWbs();
//					BeanUtils.copyProperties(wbsModelEntity, projectWbsEntity);
//					projectWbsEntity.setId(null);
//					projectWbsEntity.setProjectId(projectId);
//					projectWbsEntity.setWbsId(wbsModelId);
//					projectWbsList.add(projectWbsEntity);
//				}
//			}
		}

//		if (projectWbsList.size() > 0) {
//			projectWbsService.saveBatch(projectWbsList);
//		}

	}
	
    /**
     * 
     * @param oldParentId 原模板父节点ID
     * @param newParentId 新父节点ID
     * @param projectId  项目ID
     * @param wbsModelId 原模板ID
     */
	public void saveProjectChildNode(String oldParentId,String newParentId,String projectId,String wbsModelId){
		
		List<WbsModel> wbsModelList = wbsModelService.getChilNode(oldParentId);
		
        for (WbsModel wbsModel : wbsModelList) {
        	
        	ProjectWbs projectWbs = new ProjectWbs();
			BeanUtils.copyProperties(wbsModel, projectWbs);
			projectWbs.setId(null);
			projectWbs.setProjectId(projectId);
			projectWbs.setParentId(newParentId);
			projectWbs.setWbsId(wbsModelId);

			projectWbsService.save(projectWbs);
			
        	//递归,获取子节点
        	this.saveProjectChildNode(wbsModel.getId(),projectWbs.getId(),projectId,wbsModelId);
        }
       
	}

	/**
	 * 项目编辑
	 * 
	 * @author huangheng
	 * @param projectSaveEntity
	 */
	@Transactional
	public void edit(ProjectSaveEntity projectSaveEntity) {

		Project project = new Project();
		BeanUtils.copyProperties(projectSaveEntity, project);

		if (null == project.getContractId()) {
			throw new ErpException("合同ID不能为空");
		}
		// 1、修改项目信息表
		this.updateById(project);

		String projectId = project.getId();

		// 2、更新费用信息:先删除,根据ID查询记录是否存在，存在则更新,否则新增
		projectCostService.deleteBatchByMainId(projectId);

		List<ProjectCost> projectCostList = projectSaveEntity.getProjectCostList();
		for (ProjectCost projectCost : projectCostList) {

			ProjectCost projectCostEntity = projectCostService.selectCostById(projectCost.getId());
			if (null == projectCostEntity) {
				projectCost.setId(null);
				projectCost.setProjectId(projectId);
				projectCostService.save(projectCost);
			} else {
				// 更新状态
				projectCostService.updateDelFlag(projectCostEntity.getId());

				// 更新金额
				projectCostEntity.setWbsTypeId(projectCost.getWbsTypeId());
				projectCostEntity.setCost(projectCost.getCost());
				projectCostService.updateById(projectCostEntity);
			}
		}

		// 3、更新附件信息
		attachedUrlService.deleteByRelationId(projectId);

		String fileUrl = projectSaveEntity.getUrl();
		if (!StringUtil.isNullOrEmpty(fileUrl)) {
			if (fileUrl.contains(",")) {
				String[] url = fileUrl.split(",");
				for (int index = 0; index < url.length; index++) {

					AttachedUrl attachedUrlEntity = attachedUrlService.selectOne(projectId, url[index], index + 1);
					if (null == attachedUrlEntity) {
						AttachedUrl attachedUrl = new AttachedUrl();
						attachedUrl.setRelationId(projectId);
						attachedUrl.setUrl(url[index]);
						attachedUrl.setSort(index + 1);
						attachedUrlMapper.insert(attachedUrl);
					} else {
						// 更新状态为0-有效
						attachedUrlService.updateDelFlagById(attachedUrlEntity.getId());
					}
				}
			} else {
				AttachedUrl attachedUrlEntity = attachedUrlService.selectOne(projectId, fileUrl, 1);
				if (null == attachedUrlEntity) {
					AttachedUrl attachedUrl = new AttachedUrl();
					attachedUrl.setRelationId(projectId);
					attachedUrl.setUrl(fileUrl);
					attachedUrl.setSort(1);
					attachedUrlMapper.insert(attachedUrl);
				} else {
					// 更新状态为0-有效
					attachedUrlService.updateDelFlagById(attachedUrlEntity.getId());
				}
			}
		}

		// 4、更新项目wbs结构
		Boolean addProjectWbsFlag = false;
		String wbsModelId = projectSaveEntity.getWbsId();

		List<ProjectWbs> projectWbsList = projectWbsService.getProjectWbsByProjectId(projectId);
		if (null != projectWbsList && projectWbsList.size() > 0) {
			ProjectWbs projectWbs = projectWbsList.get(0);
			if (!projectWbs.getWbsId().equals(wbsModelId)) {
				// wbs已变更
				projectWbsService.deleteByProjectId(projectId);
				addProjectWbsFlag = true;
			}
		} else {
			// 直接新增
			addProjectWbsFlag = true;
		}
		if (addProjectWbsFlag) {
			// 新增项目分解结构
			//List<ProjectWbs> projectWbss = new ArrayList<ProjectWbs>();

			WbsModel wbsModel = wbsModelService.getById(wbsModelId);
			if (null != wbsModel) {
				ProjectWbs projectWbs = new ProjectWbs();
				BeanUtils.copyProperties(wbsModel, projectWbs);
				projectWbs.setId(null);
				projectWbs.setProjectId(projectId);
				projectWbs.setWbsId(wbsModelId);
				//projectWbss.add(projectWbs);
				projectWbsService.save(projectWbs);
				
				this.saveProjectChildNode(wbsModel.getId(),projectWbs.getId(),projectId,wbsModelId);
				
				// 获取所有子节点
//				List<WbsModel> wbsModelList = new ArrayList<WbsModel>();
//				wbsModelList = wbsModelService.getChilNode(wbsModelList,wbsModel.getId());
//				if (wbsModelList.size() > 0) {
//					for (WbsModel wbsModelEntity : wbsModelList) {
//						ProjectWbs projectWbsEntity = new ProjectWbs();
//						BeanUtils.copyProperties(wbsModelEntity, projectWbsEntity);
//						projectWbsEntity.setId(null);
//						projectWbsEntity.setProjectId(projectId);
//						projectWbsEntity.setWbsId(wbsModelId);
//						projectWbss.add(projectWbsEntity);
//					}
//				}
			}


//			if (projectWbss.size() > 0) {
//				projectWbsService.saveBatch(projectWbss);
//			}
		}
	}

	/**
	 * 封装list接口返回信息
	 * 
	 * @param projectList
	 * @return
	 */
	public List<Project> setContractInfo(List<Project> projectList) {
		for (Project project : projectList) {
			PsContract psContract = contractService.getById(project.getContractId());
			if (null != psContract) {
				project.setOperator(psContract.getOperator());
				project.setPartyAName(psContract.getPartyAName());
				project.setPartyBName(psContract.getPartyBName());
			}
		}
		return projectList;
	}

	/**
	 * 新增项目审核通过
	 * 
	 * @param projectList
	 * @return
	 */
	@Transactional
	public void check(List<Project> projectList) {
		for (Project project : projectList) {
			if (project.getStatus().equals(TpsProjectStatus.PENDING.getCode())) {
				project.setStatus(TpsProjectStatus.APPROVE.getCode());
				this.updateById(project);
			}
		}

	}

	/**
	 * 获取项目详细信息
	 * 
	 * @param id
	 * @return
	 */
	public ProjectDetail getProjectInfo(String id) {
		ProjectDetail projectDetail = new ProjectDetail();
		Project project = this.getById(id);
		if (null != project) {

			BeanUtils.copyProperties(project, projectDetail);
			PsContract psContract = contractService.getById(project.getContractId());
			if (null != psContract) {
				BeanUtils.copyProperties(psContract, projectDetail);
			}

			List<ProjectWbs> projectWbsList = projectWbsService.getProjectWbsByProjectId(project.getId());
			if (null != projectWbsList && projectWbsList.size() > 0) {
				projectDetail.setWbsId(projectWbsList.get(0).getWbsId());
			}
		}
		return projectDetail;
	}

	/**
	 * 获取费用明细
	 * 
	 * @param id 项目ID
	 * @return
	 */
	public List<ProjectCostInfo> queryProjectCostList(String wbsId, String projectId) {
		List<ProjectCostInfo> projectCostInfos = new ArrayList<ProjectCostInfo>();
		if (null == projectId) {
			// 新增时,直接从wbs模板获取,预想树形最多5层
			// 获取第二层
			List<ProjectCostInfo> projectCostInfoList = wbsModelService.queryWbsChildByParentId(wbsId);
			for (ProjectCostInfo projectCostInfo : projectCostInfoList) {
				// 获取第三层
				List<ProjectCostInfo> list1 = wbsModelService.queryWbsChildByParentId(projectCostInfo.getId());
				if (list1.size() > 0) {
					for (ProjectCostInfo projectCostInfo1 : list1) {
						// 获取第四层
						List<ProjectCostInfo> list2 = wbsModelService.queryWbsChildByParentId(projectCostInfo1.getId());
						if (list2.size() > 0) {
							for (ProjectCostInfo projectCostInfo2 : list2) {
								// 获取第五层
								List<ProjectCostInfo> list3 = wbsModelService
										.queryWbsChildByParentId(projectCostInfo2.getId());
								if (list3.size() > 0) {
									for (ProjectCostInfo projectCostInfo3 : list3) {
										if (!StringUtil.isNullOrEmpty(projectCostInfo3.getWbsTypeId())) {
											projectCostInfos.add(projectCostInfo3);
										}
									}
								} else {
									if (!StringUtil.isNullOrEmpty(projectCostInfo2.getWbsTypeId())) {
										projectCostInfos.add(projectCostInfo2);
									}
								}
							}
						} else {
							if (!StringUtil.isNullOrEmpty(projectCostInfo1.getWbsTypeId())) {
								projectCostInfos.add(projectCostInfo1);
							}
						}
					}
				} else {
					if (!StringUtil.isNullOrEmpty(projectCostInfo.getWbsTypeId())) {
						projectCostInfos.add(projectCostInfo);
					}
				}
			}
		} else {
			projectCostInfos = projectCostService.getProjectCostByProjectId(projectId);
		}
		return projectCostInfos;
	}

}
