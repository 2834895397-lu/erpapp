package com.dongxin.erp.fm.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dongxin.erp.bm.entity.CompanyOffer;
import com.dongxin.erp.cs.entity.ProfileBank;
import com.dongxin.erp.cs.entity.ProfileInf;
import com.dongxin.erp.cs.service.ProfileBankService;
import com.dongxin.erp.cs.service.ProfileInfService;

import com.dongxin.erp.fm.service.PayDtlService;
import com.dongxin.erp.fm.service.PayInfService;
import com.dongxin.erp.mm.service.ContractDtlService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.dongxin.erp.fm.entity.PayDtl;
import com.dongxin.erp.fm.entity.PayInf;
import com.dongxin.erp.fm.vo.PayInfPage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Description: 财务付款信息表
 * @Author: jeecg-boot
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Api(tags="财务付款信息表")
@RestController
@RequestMapping("/fm/payInf")
@Slf4j
public class PayInfController {
	@Autowired
	private PayInfService payInfService;
	@Autowired
	private PayDtlService payDtlService;
	@Autowired
	private ProfileBankService profileBankService;
	@Autowired
	private ProfileInfService profileInfService;
	@Autowired
	private ContractDtlService contractDtlService;
	
	/**
	 * 分页列表查询
	 *
	 * @param payInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-分页列表查询")
	@ApiOperation(value="财务付款信息表-分页列表查询", notes="财务付款信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(PayInf payInf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PayInf> queryWrapper = QueryGenerator.initQueryWrapper(payInf, req.getParameterMap());
		Page<PayInf> page = new Page<PayInf>(pageNo, pageSize);
		IPage<PayInf> pageList = payInfService.page(page, queryWrapper);
		//模糊查询,范围查询
		IPage<PayInf> payInfIPage = pageList.setRecords(payInfService.payInfQuery(payInf, pageList.getRecords()));
		//显示款项性质名称
		Map<String, String> idAndNameOfMaps = payInfService.selectPayNature();
		for(PayInf payInf1:payInfIPage.getRecords()){
			String id=payInf1.getPayNature();
			payInf1.setPayNatureName(idAndNameOfMaps.get(id));
		}


		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param payInfPage
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-添加")
	@ApiOperation(value="财务付款信息表-添加", notes="财务付款信息表-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("fm:payInf:add")
	public Result<?> add(@RequestBody PayInfPage payInfPage) {
		PayInf payInf = new PayInf();
		boolean b=payInfService.add(payInfPage);
		//计算合同总金额
		/*String id=payInfPage.getContractId();
		BigDecimal price=contractDtlService.selectprice(id);
     	//计算付款总金额
		BigDecimal sum=new BigDecimal(0);
		for (int i = 0; i < payInfPage.getPayDtlList().size(); i++) {
			sum = sum.add(payInfPage.getPayDtlList().get(i).getMoney());
		}
		//比较合同总金额和付款总金额
		if(price.compareTo(sum) > -1) {
			payInfPage.setTotalSum(sum);*/
            if(b){
			BeanUtils.copyProperties(payInfPage, payInf);
			payInfService.saveMain(payInf, payInfPage.getPayDtlList());
			return Result.ok("添加成功！");
		}else{
			return Result.error("付款总金额不能超过采购合同总金额");
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param payInfPage
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-编辑")
	@ApiOperation(value="财务付款信息表-编辑", notes="财务付款信息表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("fm:payInf:edit")
	public Result<?> edit(@RequestBody PayInfPage payInfPage) {
		PayInf payInf = new PayInf();
		//计算合同总金额
		/*String id=payInfPage.getContractId();
		BigDecimal price=contractDtlService.selectprice(id);
		//计算付款总金额
		BigDecimal sum=new BigDecimal(0);
		for (int i = 0; i < payInfPage.getPayDtlList().size(); i++) {
			sum = sum.add(payInfPage.getPayDtlList().get(i).getMoney());
		}
		//比较合同总金额和付款总金额
		if(price.compareTo(sum) > -1) {
			payInfPage.setTotalSum(sum);*/
		boolean b=payInfService.add(payInfPage);
		if(b){
			BeanUtils.copyProperties(payInfPage, payInf);
			PayInf payInfEntity = payInfService.getById(payInf.getId());
			if (payInfEntity == null) {
				return Result.error("未找到对应数据");
			}

			payInfService.updateMain(payInf, payInfPage.getPayDtlList());
			return Result.ok("编辑成功!");
		}else{
			return Result.error("付款总金额不能超过采购合同总金额");
		}

	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-通过id删除")
	@ApiOperation(value="财务付款信息表-通过id删除", notes="财务付款信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("fm:payInf:delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		payInfService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-批量删除")
	@ApiOperation(value="财务付款信息表-批量删除", notes="财务付款信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("fm:payInf:batchDel")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.payInfService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "财务付款信息表-通过id查询")
	@ApiOperation(value="财务付款信息表-通过id查询", notes="财务付款信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		PayInf payInf = payInfService.getById(id);
		if(payInf==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(payInf);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "财务付款明细信息-通过主表ID查询")
	@ApiOperation(value="财务付款明细信息-通过主表ID查询", notes="财务付款明细信息-通过主表ID查询")
	@GetMapping(value = "/queryPayDtlByMainId")
	public Result<?> queryPayDtlListByMainId(@RequestParam(name="id",required=true) String id) {
		List<PayDtl> payDtlList = payDtlService.selectByMainId(id);
		IPage <PayDtl> page = new Page<>();
		page.setRecords(payDtlList);
		page.setTotal(payDtlList.size());
		return Result.ok(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param payInf
    */
    @RequestMapping(value = "/exportXls")
	@RequiresPermissions("fm:payInf:export")
    public ModelAndView exportXls(HttpServletRequest request, PayInf payInf) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<PayInf> queryWrapper = QueryGenerator.initQueryWrapper(payInf, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<PayInf> queryList = payInfService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<PayInf> payInfList = new ArrayList<PayInf>();
      if(oConvertUtils.isEmpty(selections)) {
          payInfList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          payInfList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<PayInfPage> pageList = new ArrayList<PayInfPage>();
      for (PayInf main : payInfList) {
          PayInfPage vo = new PayInfPage();
          BeanUtils.copyProperties(main, vo);
          List<PayDtl> payDtlList = payDtlService.selectByMainId(main.getId());
          vo.setPayDtlList(payDtlList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "财务付款信息表列表");
      mv.addObject(NormalExcelConstants.CLASS, PayInfPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("财务付款信息表数据", "导出人:"+sysUser.getRealname(), "财务付款信息表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }
    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@RequiresPermissions("fm:payInf:import")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<PayInfPage> list = ExcelImportUtil.importExcel(file.getInputStream(), PayInfPage.class, params);
              for (PayInfPage page : list) {
                  PayInf po = new PayInf();
                  BeanUtils.copyProperties(page, po);
                  payInfService.saveMain(po, page.getPayDtlList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

//取到支付账号
	 @GetMapping("/getPayAccount")
	 public Result getPayAccount(@RequestParam(name = "id") String id){
         List<ProfileBank> profileBanks = profileBankService.selprofileBank(id);
         IPage<ProfileBank> page = new Page<>();
         page.setRecords(profileBanks);
         page.setTotal(profileBanks.size());
         return Result.ok(page);
     }

//     //收款开户行后台
	 @AutoLog(value = "财务付款信息表-通过id查询")
	 @ApiOperation(value="财务付款信息表-通过id查询", notes="财务付款信息表-通过id查询")
	 @GetMapping(value = "/getReceptBank")
	 public Result<?> getReceptBank(@RequestParam(name="id",required=true) String id) {
		 List<ProfileBank> profileBanks = profileBankService.selprofileBank(id);
		 IPage<ProfileBank> page = new Page<>();
		 page.setRecords(profileBanks);
		 page.setTotal(profileBanks.size());
		 return Result.OK(page);
	 }
	 //取到收款账号后台
	 @GetMapping(value = "/getReceptAccount")
	 public Result<?> getReceptAccount(@RequestParam(name="id",required=true) String id) {
		 List<ProfileBank> profileBanks = profileBankService.selprofileBankAccount(id);
		 IPage<ProfileBank> page = new Page<>();
		 page.setRecords(profileBanks);
		 page.setTotal(profileBanks.size());
		 return Result.OK(page);
	 }
	 @PostMapping("/check")
	 @RequiresPermissions("fm:payInf:check")
	 public Result check(@RequestBody ArrayList<PayInf> payInfs) {
		 payInfService.check(payInfs);
		 return Result.OK("审核成功!");

	 }

 }
