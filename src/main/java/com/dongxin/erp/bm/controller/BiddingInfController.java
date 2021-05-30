package com.dongxin.erp.bm.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dongxin.erp.bd.service.MaterialService;
import com.dongxin.erp.bm.entity.*;
import com.dongxin.erp.bm.service.BiddingPriceService;
import com.dongxin.erp.chart.vo.MultiBar;
import com.dongxin.erp.enums.BiddingFlag;
import com.dongxin.erp.enums.BiddingStatus;
import com.dongxin.erp.exception.ErpException;
import com.dongxin.erp.fm.entity.PayInf;
import com.dongxin.erp.mm.service.PlanDtlService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.config.mybatis.TenantContext;
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
import com.dongxin.erp.bm.vo.BiddingInfPage;
import com.dongxin.erp.bm.service.BiddingInfService;
import com.dongxin.erp.bm.service.BiddingDtlService;
import com.dongxin.erp.bm.service.BiddingEnterpriseService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 招标信息表
 * @Author: jeecg-boot
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Api(tags = "招标信息表")
@RestController
@RequestMapping("/bm/biddingInf")
@Slf4j
public class BiddingInfController {

	@Autowired
	private BiddingInfService biddingInfService;

	@Autowired
	private BiddingDtlService biddingDtlService;

	@Autowired
	private BiddingEnterpriseService biddingEnterpriseService;
	@Autowired
	private PlanDtlService planDtlService;
	@Autowired
	private BiddingPriceService biddingPriceService;
	@Autowired
	private MaterialService materialService;

	/**
	 * 分页列表查询
	 *
	 * @param biddingInf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "招标信息表-分页列表查询")
	@ApiOperation(value = "招标信息表-分页列表查询", notes = "招标信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BiddingInf biddingInf,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		// 截标时间赋值为空
		Date date = biddingInf.getBTime();
		Date date2 = biddingInf.getETime();
		biddingInf.setETime(null);
		biddingInf.setBTime(null);

		QueryWrapper<BiddingInf> queryWrapper = QueryGenerator.initQueryWrapper(biddingInf, req.getParameterMap());
		Page<BiddingInf> page = new Page<BiddingInf>(pageNo, pageSize);
		IPage<BiddingInf> pageList = biddingInfService.page(page, queryWrapper);
		// 开标时间,截标时间范围查询,标题,申请单位模糊查询
		pageList.setRecords(biddingInfService.biddingQuery(biddingInf, date, date2, pageList.getRecords()));

		/*
		 * List<BiddingInf> biddingInfs=biddingInfService.listBiddingInf(biddingInf);
		 * pageList.setRecords(biddingInfs);
		 */
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param biddingInfPage
	 * @return
	 */
	@AutoLog(value = "招标信息表-添加")
	@ApiOperation(value = "招标信息表-添加", notes = "招标信息表-添加")
	@PostMapping(value = "/add")
	//权限验证
	@RequiresPermissions("bm:biddingInf:add")
	public Result<?> add(@RequestBody BiddingInfPage biddingInfPage) {
		biddingInfService.add(biddingInfPage);
		return Result.OK("添加成功！");

	}

	@AutoLog(value = "报价信息表-添加")
	@ApiOperation(value = "报价信息表-添加", notes = "报价信息表-添加")
	@PostMapping(value = "/addPriceForm")
	public Result<?> addPriceForm(@RequestBody List<CompanyOffer> companyOffer,
			@RequestParam(name = "companyId") String companyId, @RequestParam(name = "date") String date)
			throws ParseException {

		Boolean ok = biddingPriceService.saveBiddingPrice(companyOffer, companyId, date);
		if (ok) {

			return Result.OK("添加成功！");
		} else {

			return Result.error("添加失败!!!");
		}

	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "招标信息表-通过id删除")
	@ApiOperation(value = "招标信息表-通过id删除", notes = "招标信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	@RequiresPermissions("bm:biddingInf:delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		biddingInfService.delete(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "招标信息表-批量删除")
	@ApiOperation(value = "招标信息表-批量删除", notes = "招标信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	@RequiresPermissions("")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.biddingInfService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "招标信息表-通过id查询")
	@ApiOperation(value = "招标信息表-通过id查询", notes = "招标信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		BiddingInf biddingInf = biddingInfService.getById(id);
		if (biddingInf == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(biddingInf);

	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "招标明细信息表-通过主表ID查询")
	@ApiOperation(value = "招标明细信息表-通过主表ID查询", notes = "招标明细信息表-通过主表ID查询")
	@GetMapping(value = "/queryBiddingDtlByMainId")
	public Result<?> queryBiddingDtlListByMainId(@RequestParam(name = "id", required = true) String id) {
		List<BiddingDtl> biddingDtlList = biddingDtlService.selectByMainId(id);
		IPage<BiddingDtl> page = new Page<>();

		page.setRecords(biddingDtlList);
		page.setTotal(biddingDtlList.size());
		return Result.ok(page);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "招标企业信息表-通过主表ID查询")
	@ApiOperation(value = "招标企业信息表-通过主表ID查询", notes = "招标企业信息表-通过主表ID查询")
	@GetMapping(value = "/queryBiddingEnterpriseByMainId")
	public Result<?> queryBiddingEnterpriseListByMainId(@RequestParam(name = "id", required = true) String id) {
		List<BiddingEnterprise> biddingEnterpriseList = biddingEnterpriseService.selectByMainId(id);
		IPage<BiddingEnterprise> page = new Page<>();
		page.setRecords(biddingEnterpriseList);
		page.setTotal(biddingEnterpriseList.size());
		return Result.ok(page);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param biddingInf
	 */
	@RequestMapping(value = "/exportXls")
	@RequiresPermissions("bm:biddingInf:export")
	public ModelAndView exportXls(HttpServletRequest request, BiddingInf biddingInf) {
		// Step.1 组装查询条件查询数据
		QueryWrapper<BiddingInf> queryWrapper = QueryGenerator.initQueryWrapper(biddingInf, request.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		// Step.2 获取导出数据
		List<BiddingInf> queryList = biddingInfService.list(queryWrapper);
		// 过滤选中数据
		String selections = request.getParameter("selections");
		List<BiddingInf> biddingInfList = new ArrayList<BiddingInf>();
		if (oConvertUtils.isEmpty(selections)) {
			biddingInfList = queryList;
		} else {
			List<String> selectionList = Arrays.asList(selections.split(","));
			biddingInfList = queryList.stream().filter(item -> selectionList.contains(item.getId()))
					.collect(Collectors.toList());
		}

		// Step.3 组装pageList
		List<BiddingInfPage> pageList = new ArrayList<BiddingInfPage>();
		for (BiddingInf main : biddingInfList) {
			BiddingInfPage vo = new BiddingInfPage();
			BeanUtils.copyProperties(main, vo);
			List<BiddingDtl> biddingDtlList = biddingDtlService.selectByMainId(main.getId());
			vo.setBiddingDtlList(biddingDtlList);
			List<BiddingEnterprise> biddingEnterpriseList = biddingEnterpriseService.selectByMainId(main.getId());
			vo.setBiddingEnterpriseList(biddingEnterpriseList);
			pageList.add(vo);
		}

		// Step.4 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "招标信息表列表");
		mv.addObject(NormalExcelConstants.CLASS, BiddingInfPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("招标信息表数据", "导出人:" + sysUser.getRealname(), "招标信息表"));
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
				List<BiddingInfPage> list = ExcelImportUtil.importExcel(file.getInputStream(), BiddingInfPage.class,
						params);
				for (BiddingInfPage page : list) {
					BiddingInf po = new BiddingInf();
					BeanUtils.copyProperties(page, po);
					biddingInfService.saveMain(po, page.getBiddingDtlList(), page.getBiddingEnterpriseList());
				}
				return Result.ok("文件导入成功！数据行数:" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
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

	// 获得企业id
	@PostMapping("/getCompanyOffer")
	@RequiresPermissions("bm:biddingEnterprise:price")
	public List<CompanyOffer> getCompanyOffer(@RequestBody BiddingEnterprise biddingEnterprise) {
		List<CompanyOffer> companyOffers = biddingDtlService.getCompanyOffer(biddingEnterprise);
		// 显示计量单位名称
		Map<String, String> idAndNameOfMaps = biddingDtlService.selectUnit();
		for (CompanyOffer companyOffer : companyOffers) {
			String mea_no = companyOffer.getMeasureUnit();
			companyOffer.setMeasureUnitName(idAndNameOfMaps.get(mea_no));
		}

		return companyOffers;
	}

	/**
	 * 获得采购申请明细
	 * 
	 * @param biddingInfId 招标编号
	 * @return
	 */
	@GetMapping("/purchaseDetail")
	@RequiresPermissions("bm:biddingDtl:list")
	public List<BiddingDtl> getplanDetail(@RequestParam(name = "id") String biddingInfId) {
		List<BiddingDtl> biddingDtls = planDtlService.getPurchaseDetail(biddingInfId);
		return biddingDtls;
	}

	/**
	 * 添加物料明细
	 * 
	 * @param biddingDtl
	 * @param id
	 * @return
	 */
	@PostMapping("/addMaterialDtl")
	public Result<Object> addMaterialDtl(@RequestBody List<BiddingDtl> biddingDtl,
			@RequestParam(name = "id") String id) {
		try {
			biddingDtlService.addMaterialDtl(biddingDtl, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("添加失败,请检查");
		}
		return Result.OK("添加成功!");
	}
	
	/**
	 * 编辑
	 * 
	 * @param biddingInfPage
	 * @return
	 */
	@AutoLog(value = "招标信息表-编辑")
	@ApiOperation(value = "招标信息表-编辑", notes = "招标信息表-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("bm:biddingInf:edit")
	public Result<Object> edit(@RequestBody BiddingInfPage biddingInfPage) {
		try {
			biddingInfService.edit(biddingInfPage);
		} catch (ErpException e) {
			return Result.error(e.getMessage());
		}
		return Result.OK("编辑成功!");

	}

	// 采购申请号模糊查询
	@GetMapping("/listPlanNo")
	public List<BiddingDtl> listPlanNo(BiddingDtl biddingDtl) {
		List<BiddingDtl> biddingDtls = biddingDtlService.listPlanNo(biddingDtl);
		return biddingDtls;
	}

	/**
	 * 招标报价信息表
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "招标报价信息表", notes = "招标报价信息表")
	@GetMapping(value = "/listBiddingPrice")
	@RequiresPermissions("bm:biddingPrice:list")
	public Result<?> listBiddingPrice(@RequestParam(name = "id", required = true) String id) {
		return Result.ok(biddingInfService.listBiddingPrice(id));
	}

	@GetMapping("/status")
	@RequiresPermissions("bm:biddingEnterprise:bidding")
	public Result status(@RequestParam(name = "id", required = true) String id) {

		List<BiddingPrice> biddingPrices = biddingEnterpriseService.selectBiddingPrice(id);

		if (biddingPrices.size() > 0) {
			// 企业中标状态改变
			BiddingEnterprise biddingEnterprise = biddingEnterpriseService.getById(id);
			Date date = new Date();
			String biddingStatus = biddingEnterprise.getBiddingStatus();
			if (biddingStatus.equals(BiddingFlag.BIDPENDING.getCode())) {
				biddingEnterprise.setBiddingStatus(BiddingFlag.BIDWIN.getCode());
				biddingEnterprise.setBiddingDate(date);
				biddingEnterpriseService.saveOrUpdate(biddingEnterprise);
				// 招标管理中标状态
				BiddingInf biddingInf = biddingInfService.getById(biddingEnterprise.getBiddingInfId());
				biddingInf.setBiddingFlag(BiddingStatus.BIDWIN.getCode());
				biddingInfService.saveOrUpdate(biddingInf);
				return Result.OK("执行成功");
			} else {
				return Result.error("已经为中标状态");
			}
		}
		return Result.error("无报价");

	}
}
