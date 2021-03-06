package com.dongxin.erp.mm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollUtil;
import com.dongxin.erp.bd.entity.Material;
import com.dongxin.erp.bd.entity.MaterialType;
import com.dongxin.erp.bd.mapper.MaterialMapper;
import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.bd.service.MaterialTypeService;
import com.dongxin.erp.cs.mapper.ProfileInfMapper;
import com.dongxin.erp.cs.service.ProfileInfService;
import com.dongxin.erp.fm.entity.PayInf;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.checkerframework.checker.units.qual.C;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.mm.entity.ContractDtl;
import com.dongxin.erp.mm.entity.Contract;
import com.dongxin.erp.mm.vo.ContractPage;
import com.dongxin.erp.mm.service.ContractService;
import com.dongxin.erp.mm.service.ContractDtlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ??????????????????
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Api(tags="??????????????????")
@RestController
@RequestMapping("/mm/contract")
@Slf4j
public class ContractController {

	@Autowired
    private ContractService contractService;

    @Autowired
    private ContractDtlService contractDtlService;

    @Autowired
	 ProfileInfService profileInfService;
    @Autowired
	 MaterialService materialService;
    @Autowired
	 MaterialTypeService materialTypeService;
    @Autowired
	 MaterialMapper materialMapper;

    @Autowired
    @Qualifier(value="profileInfMapper")
	 ProfileInfMapper profileInfMapper;
	
	/**
	 * ??????????????????
	 *
	 * @param contract
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "??????????????????-??????????????????")
	@ApiOperation(value="??????????????????-??????????????????", notes="??????????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Contract contract,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Contract> queryWrapper = QueryGenerator.initQueryWrapper(contract, req.getParameterMap());
		Page<Contract> page = new Page<Contract>(pageNo, pageSize);
		IPage<Contract> pageList = contractService.page(page, queryWrapper);




		return Result.ok(pageList);
	}

	/**
	 *   ??????
	 *
	 * @param contractPage
	 * @return
	 */
	@RequiresPermissions("contract.add")
	@AutoLog(value = "??????????????????-??????")
	@ApiOperation(value="??????????????????-??????", notes="??????????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ContractPage contractPage) {
		Contract contract = new Contract();
		//?????????????????????????????????
		BeanUtils.copyProperties(contractPage, contract);
		List<String> contractPageMTIDS = contractPage.getContractDtlList().stream().map(ContractDtl::getTbdMaterialId).filter(e -> !e.equals("")).collect(Collectors.toList());
		String purchaseOrgn = contractPage.getPurchaseOrgn();
		QueryWrapper<MaterialType> materialTypeQW = new QueryWrapper<>();
		materialTypeQW.eq("org_id",purchaseOrgn);
		List<String> ids = materialTypeService.list(materialTypeQW).stream().map(MaterialType::getId).collect(Collectors.toList());
		if(ids.size() == 0){
			return Result.error("???????????????????????????????????????");
		}
		QueryWrapper<Material> materialQW = new QueryWrapper<>();
		materialQW.in("material_type_id",ids);
		List<String> materialIds = materialService.list(materialQW).stream().map(Material::getId).collect(Collectors.toList());
		boolean b = materialIds.containsAll(contractPageMTIDS);
		if(b){
			contractService.saveMain(contract,contractPage.getContractDtlList());
			return Result.ok("????????????");
		}else {
			contractPageMTIDS.removeAll(materialIds);
			List<String> names = materialService.listByIds(contractPageMTIDS).stream().map(Material::getName).collect(Collectors.toList());
			return Result.error(names.toString().replace("[","").replace("]","")+"??????????????????!");

		}

		/*Contract contract = new Contract();
		String orgId=contractPage.getPurchaseOrgn();
		List<Material> materials=materialService.selectMaterialId(orgId);
		if(CollUtil.isNotEmpty(materials)) {
			for (Material material : materials) {
				String pid = material.getPid();
				while (!pid.equals("0")) {
                  String materialId=material.getId();
                  List<Material> materialList=materialMapper.selectMaterialId(materialId);

				}
			}
		}*/
		/*if(materials!=null && materials.size()!=0){
		for (int i = 0; i < materials.size(); i++) {
			String materialId = materials.get(i).getId();
			List<Material> materialList = materialMapper.selectMaterialPid(materialId);
			if(CollUtil.isNotEmpty(materialList)) {
				for (int j = 0; j < materialList.size(); j++) {
					List<Material> materiaId = materialMapper.selectMaterialPid(materialList.get(j).getId());

					List<String> materialNames=new ArrayList<>();
					for (int k = 0; k < contractPage.getContractDtlList().size(); k++) {
						String tbdMaterialId = contractPage.getContractDtlList().get(k).getTbdMaterialId();
						if (!materiaId.equals(tbdMaterialId)) {
                             materialNames.add(contractPage.getContractDtlList().get(k).getTbdMaterialId());
						}
					}

					if(CollUtil.isNotEmpty(materialNames)){
						return Result.OK(materialNames);
					}
				}
			}
		}

		}*/
	/*	List<MaterialType> b=materialTypeService.selectMaterialType(orgId);

		if(b!=null && b.size()!=0) {
			for (int i = 0; i < contractPage.getContractDtlList().size(); i++) {
				String materialId = contractPage.getContractDtlList().get(i).getTbdMaterialId();
				String a = materialService.selectMaterial(materialId);
				List<String> materialName = new ArrayList<>();
				if (a != null) {
					for (int j = 0; j < b.size(); j++) {
						String c = b.get(j).getId();
						if (!a.equals(c)) {
							materialName.add(contractPage.getContractDtlList().get(j).getTbdMaterialId());
						}
					}
				}

				if (CollUtil.isNotEmpty(materialName)) {
					return Result.ok(materialName);
				}
			}
		}
*/

/*
		BeanUtils.copyProperties(contractPage, contract);
		contractService.saveMain(contract, contractPage.getContractDtlList());
		return Result.ok("???????????????");*/


	}
	
	/**
	 *  ??????
	 *
	 * @param contractPage
	 * @return
	 */
	@AutoLog(value = "??????????????????-??????")
	@ApiOperation(value="??????????????????-??????", notes="??????????????????-??????")
	@PutMapping(value = "/edit")
	@RequiresPermissions("contract.edit")
	public Result<?> edit(@RequestBody ContractPage contractPage) {
		Contract contract = new Contract();
		BeanUtils.copyProperties(contractPage, contract);
		Contract contractEntity = contractService.getById(contract.getId());
		if(contractEntity==null) {
			return Result.error("?????????????????????");
		}
		contractService.updateMain(contract, contractPage.getContractDtlList());
		return Result.ok("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "??????????????????-??????id??????")
	@ApiOperation(value="??????????????????-??????id??????", notes="??????????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("contract.delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		String s = contractService.delMain(id);
		return Result.ok(s);
	}

	 /**
	  * ????????????ids?????????????????????????????????
	  *
	  * @param ids: ?????????ids, ??????id???,??????
	  * @return
	  */
	 @RequiresPermissions("contract.batchDelete")
	@AutoLog(value = "??????????????????-????????????")
	@ApiOperation(value="??????????????????-????????????", notes="??????????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		int i = contractService.delBatchMain(Arrays.asList(ids.split(",")));
		if(i == 0){
			return Result.error("?????????????????????????????????");
		}

		return Result.ok("????????????" + i + "?????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "??????????????????-??????id??????")
	@ApiOperation(value="??????????????????-??????id??????", notes="??????????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Contract contract = contractService.getById(id);
		if(contract==null) {
			return Result.error("?????????????????????");
		}
		return Result.ok(contract);

	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "?????????????????????-????????????ID??????")
	@ApiOperation(value="?????????????????????-????????????ID??????", notes="?????????????????????-????????????ID??????")
	@GetMapping(value = "/queryContractDtlByMainId")
	public Result<?> queryContractDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ContractDtl> contractDtlList = contractDtlService.selectByMainId(id);

		//??????????????????
		contractDtlService.setPlanNumber(contractDtlList);


		IPage <ContractDtl> page = new Page<>();
		page.setRecords(contractDtlList);
		page.setTotal(contractDtlList.size());

		return Result.ok(page);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param contract
    */
    @RequestMapping(value = "/exportXls")
	@RequiresPermissions("contract.download")
    public ModelAndView exportXls(HttpServletRequest request, Contract contract) {
      // Step.1 ??????????????????????????????
      QueryWrapper<Contract> queryWrapper = QueryGenerator.initQueryWrapper(contract, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<Contract> queryList = contractService.list(queryWrapper);
      // ??????????????????
      String selections = request.getParameter("selections");
      List<Contract> contractList = new ArrayList<Contract>();
      if(oConvertUtils.isEmpty(selections)) {
          contractList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          contractList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 ??????pageList
      List<ContractPage> pageList = new ArrayList<ContractPage>();
      for (Contract main : contractList) {
          ContractPage vo = new ContractPage();
          BeanUtils.copyProperties(main, vo);
          List<ContractDtl> contractDtlList = contractDtlService.selectByMainId(main.getId());
          vo.setContractDtlList(contractDtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "????????????????????????");
      mv.addObject(NormalExcelConstants.CLASS, ContractPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????????????????", "?????????:"+sysUser.getRealname(), "??????????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * ??????excel????????????
    *
    * @param request
    * @param response
    * @return
    */
	@RequiresPermissions("contract.import")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// ????????????????????????
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ContractPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ContractPage.class, params);
              for (ContractPage page : list) {
                  Contract po = new Contract();
                  BeanUtils.copyProperties(page, po);
                  contractService.saveMain(po, page.getContractDtlList());
              }
              return Result.ok("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("?????????????????????");
    }
     // ????????????
	 @PostMapping("/check")
	 @RequiresPermissions("contract.check")
	 public Result check(@RequestBody ArrayList<Contract> contracts) {

		 contractService.check(contracts);
		 return Result.OK("????????????!");

	 }

	 // ????????????
	 @PostMapping("/over")
	 @RequiresPermissions("contract.over")
	 public Result over(@RequestBody ArrayList<Contract> contracts) {

		 contractService.over(contracts);
		 return Result.OK("???????????????!");

	 }
	 /**
	  * ??????id??????
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "?????????????????????-??????id??????")
	 @ApiOperation(value = "?????????????????????-??????id??????", notes = "?????????????????????-??????id??????")
	 @DeleteMapping(value = "/deleteContractDtl")
	 public Result<?> deleteContractDtl(@RequestParam(name = "id", required = true) List<String> id) {
		 contractDtlService.removeByIds(id);
		 return Result.OK("????????????!");
	 }



}
