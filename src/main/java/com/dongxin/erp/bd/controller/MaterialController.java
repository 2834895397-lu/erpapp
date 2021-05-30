package com.dongxin.erp.bd.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.dongxin.erp.bd.entity.Material;
import com.dongxin.erp.bd.service.MaterialService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 物料信息
 * @Author: jeecg-boot
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Api(tags="物料信息")
@RestController
@RequestMapping("/bd/material")
@Slf4j
public class MaterialController extends JeecgController<Material, MaterialService>{
	@Autowired
	private MaterialService materialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param material
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "物料信息-分页列表查询")
	@ApiOperation(value="物料信息-分页列表查询", notes="物料信息-分页列表查询")
	@GetMapping(value = "/rootList")
	public Result<?> queryPageList(Material material,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String parentId = material.getPid();
		if (oConvertUtils.isEmpty(parentId)) {
			parentId = "0";
		}
		material.setPid(null);
		QueryWrapper<Material> queryWrapper = QueryGenerator.initQueryWrapper(material, req.getParameterMap());
		// 使用 eq 防止模糊查询
		queryWrapper.eq("pid", parentId);
		Page<Material> page = new Page<Material>(pageNo, pageSize);
		IPage<Material> pageList = materialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
      * 获取子数据
      * @param material
      * @param req
      * @return
      */
	@AutoLog(value = "物料信息-获取子数据")
	@ApiOperation(value="物料信息-获取子数据", notes="物料信息-获取子数据")
	@GetMapping(value = "/childList")
	public Result<?> queryPageList(Material material,HttpServletRequest req) {
		QueryWrapper<Material> queryWrapper = QueryGenerator.initQueryWrapper(material, req.getParameterMap());
		List<Material> list = materialService.list(queryWrapper);
		IPage<Material> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
		return Result.OK(pageList);
	}

    /**
      * 批量查询子节点
      * @param parentIds 父ID（多个采用半角逗号分割）
      * @return 返回 IPage
      * @param parentIds
      * @return
      */
	@AutoLog(value = "物料信息-批量获取子数据")
    @ApiOperation(value="物料信息-批量获取子数据", notes="物料信息-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<Material> list = materialService.list(queryWrapper);
            IPage<Material> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }
	
	/**
	 *   添加
	 *
	 * @param material
	 * @return
	 */
	@AutoLog(value = "物料信息-添加")
	@ApiOperation(value="物料信息-添加", notes="物料信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Material material) {
		materialService.addMaterial(material);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param material
	 * @return
	 */
	@AutoLog(value = "物料信息-编辑")
	@ApiOperation(value="物料信息-编辑", notes="物料信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Material material) {
		materialService.updateMaterial(material);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料信息-通过id删除")
	@ApiOperation(value="物料信息-通过id删除", notes="物料信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		materialService.deleteMaterial(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料信息-批量删除")
	@ApiOperation(value="物料信息-批量删除", notes="物料信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.materialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料信息-通过id查询")
	@ApiOperation(value="物料信息-通过id查询", notes="物料信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Material material = materialService.getById(id);
		if(material==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(material);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param material
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Material material) {
		return super.exportXls(request, material, Material.class, "物料信息");
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
		return super.importExcel(request, response, Material.class);
    }

}
