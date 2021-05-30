package com.dongxin.erp.cs.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dongxin.erp.cs.csenum.CsFlag;
import com.dongxin.erp.cs.entity.*;
import com.dongxin.erp.cs.service.*;
import com.dongxin.erp.cs.vo.ProfileTypeLevelHis;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.TenantContext;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 客商基础信息
 * @Author: jeecg-boot
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Api(tags = "客商基础信息")
@RestController
@RequestMapping("/bcs/profileInf")
@Slf4j
public class BusinessProfileBusinInfController extends JeecgController<ProfileInf, ProfileInfService> {

    @Autowired
    private ProfileInfService profileInfService;

    @Autowired
    private ProfilePoaService profilePoaService;

    @Autowired
    private ProfileCneeService profileCneeService;

    @Autowired
    private ProfileBankService profileBankService;

    @Autowired
    private ProfileBelongService profileBelongService;

    @Autowired
    private ProfileProductService profileProductService;

    @Autowired
    private ProfileQualifiService profileQualifiService;

    @Autowired
    private ProfileTypeLevelService profileTypeLevelService;

    @Autowired
    private ProfileTypeLevelService profiletypelevelservice;

    @Autowired
    private BlacklistInfService blacklistInfService;



    /*---------------------------------主表处理-begin-------------------------------------*/

    /**
     * 分页列表查询
     *
     * @param profileInf
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */


    @AutoLog(value = "客商基础信息-分页列表查询")
    @ApiOperation(value = "客商基础信息-分页列表查询", notes = "客商基础信息-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ProfileInf profileInf,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ProfileInf> queryWrapper = QueryGenerator.initQueryWrapper(profileInf, req.getParameterMap());
        Page<ProfileInf> page = new Page<ProfileInf>(pageNo, pageSize);
        IPage<ProfileInf> pageList = profileInfService.page(page, queryWrapper);

        /*//黑名单联动
        this.profileInfService.blacklistLinkage(profileTypeLevel1s, blacklistInfs);*/


        //以下为客商标识，客商类型，客商登记字段的赋值操作 以及查询条件

        CsFlag csFlag = CsFlag.bussiness;
        List<ProfileInf> profileInfs = this.profileInfService.getCs(csFlag.getCode(), profileInf.getCsCode(), profileInf.getRegisPlace(), profileInf.getCsType(), profileInf.getCsLevel(), profileInf.getCsName(), TenantContext.getTenant());
        pageList.setRecords(profileInfs);


        //以下为翻译省市
        List<ProfileInf> profileInfList = profileInfService.translateProvinceOrCity(pageList.getRecords());
        pageList.setRecords(profileInfList);


        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param profileInf
     * @return
     */
    @AutoLog(value = "客商基础信息-添加")
    @ApiOperation(value = "客商基础信息-添加", notes = "客商基础信息-添加")
    @PostMapping(value = "/add")
    @RequiresPermissions("bcs:profile:add")
    public Result<?> add(@RequestBody ProfileInf profileInf) {
        //自动生成客商代码
        /*this.profileInfService.generalCsCode(profileInf);*/

        //设置是否启用，审核状态，以及审核人
        this.profileInfService.mainPageAdd(profileInf);

        //将数据存储到数据库
        profileInfService.save(profileInf);

        //在主表添加一条数据时自动添加该数据的客商类型表的一条初始数据
        CsFlag business = CsFlag.bussiness;
        this.profileInfService.typeAutoAdd(profileInf, business.getCode());


        //履历 主表以及客商等级表插入数据
        profileInfService.insertResume(profileInf);

        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param profileInf
     * @return
     */
    @AutoLog(value = "客商基础信息-编辑")
    @ApiOperation(value = "客商基础信息-编辑", notes = "客商基础信息-编辑")
    @PutMapping(value = "/edit")
    @RequiresPermissions("bcs.profile.edit")
    public Result<?> edit(@RequestBody ProfileInf profileInf) {
        profileInfService.updateById(profileInf);

        //履历 主表以及客商等级表插入数据
        profileInfService.insertResume(profileInf);

        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商基础信息-通过id删除")
    @ApiOperation(value = "客商基础信息-通过id删除", notes = "客商基础信息-通过id删除")
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("bcs.profile.del")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "num", required = true) String cs_flag) {

        //履历 主表以及客商等级表插入数据
        profileInfService.insertResumeWhenClickButton(id);

        //主表删除数据 如果两张表都有信息就只删除 该数据的客商类型的数据 如果只是单张表存在则直接将所有关于这表信息的的记录删除包括子表
        this.profileInfService.newDelete(id, cs_flag);

        return Result.OK("删除成功!");
    }


    /**
     * 查询已有
     *
     * @return
     */
    @GetMapping(value = "/getCsName")
    @RequiresPermissions("bcs:profile:add")
    public Result<?> queryCs(@RequestParam(name = "id", required = true) String name, @RequestParam(name = "num", required = true) String cs_flag) {

        //根据cs_flag 查询主表与客商类型表的连接数据
        List<ProfileInf> profileInfss = this.profileInfService.getCs(cs_flag, null, null, null, null, null, TenantContext.getTenant());

        //判断客户名称在查询信息中是否存在  存在则返回
        for (ProfileInf profileInf11 : profileInfss) {
            if (name.equals(profileInf11.getCsName())) {
                return Result.error("已存在该公司信息");
            }
        }

        //根据客商名称查询主表数据
        List<ProfileInf> profileInfs = profileInfService.queryMainPageByCsName(name);

        //翻译表单省市
        //如果存在
        if (CollUtil.isNotEmpty(profileInfs)) {
            //拿到翻译后的数据
            ProfileInf profileInf = profileInfService.translateFromProvinceOrCity(profileInfs);

            return Result.OK(profileInf);
        } else {
            return Result.error("没有相同数据");
        }
    }

    /**
     * 转为非正式
     *
     * @return
     */
    @GetMapping(value = "/counterCheckIt")
    @RequiresPermissions("bcs.profile.turnToInformal")
    public Result<?> counterCheck(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "cs_flag", required = true) String cs_flag) {

        String message = this.profileInfService.counterCheckIt(id, cs_flag);

        return Result.OK(message);

    }


    /**
     * 转为正式
     *
     * @return
     */

    @GetMapping(value = "/applyRegular")
    @RequiresPermissions("bcs.profile.turnToFormal")
    public Result<?> applyRegular(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "cs_flag", required = true) String cs_flag) {

        //当审核状态为未审核和审核不通过时 将审核状态设置为待审核
        String message = profileInfService.applyRegular(id, cs_flag);

        return Result.OK(message);
    }


    /**
     * 解除黑名单
     *
     * @return
     */
    @GetMapping(value = "/removeBlacklist")
    @RequiresPermissions("bcs.profile.removeBlacklist")
    public Result<?> removeBlacklist(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "cs_flag", required = true) String cs_flag) {

        //将选中的数据类型解除黑名单
        String message = profileInfService.removeBlacklist(id, cs_flag);

        return Result.OK(message);
    }

    /**
     * 转为黑名单
     *
     * @return
     */
    @GetMapping(value = "/turnToBlacklist")
    @RequiresPermissions("bcs.profile.turnToBlacklist")
    public Result<?> turnToBlacklist(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "cs_flag", required = true) String cs_flag) {

        //将选中的数据类型转为黑名单
        String message = profileInfService.turnToBlacklist(id, cs_flag);

        return Result.OK(message);
    }


    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportXls")
    @RequiresPermissions("bcs.profile.exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProfileInf profileInf) {
        return super.exportXls(request, profileInf, ProfileInf.class, "客商基础信息");
    }


    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @RequiresPermissions("bcs.profile.importExcel")
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ProfileInf.class);
    }
    /*---------------------------------主表处理-end-------------------------------------*/


    /*--------------------------------子表处理-供应商委托书-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "客商委托书-通过主表ID查询")
    @ApiOperation(value = "客商委托书-通过主表ID查询", notes = "客商委托书-通过主表ID查询")
    @GetMapping(value = "/listProfilePoaByMainId")
    public Result<?> listProfilePoaByMainId(ProfilePoa profilePoa,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpServletRequest req) {
        CsFlag csFlag = CsFlag.bussiness;
        profilePoa.setCsFlag(csFlag.getCode());
        QueryWrapper<ProfilePoa> queryWrapper = QueryGenerator.initQueryWrapper(profilePoa, req.getParameterMap());
        Page<ProfilePoa> page = new Page<ProfilePoa>(pageNo, pageSize);
        IPage<ProfilePoa> pageList = profilePoaService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param profilePoa
     * @return
     */
    @AutoLog(value = "客商委托书-添加")
    @ApiOperation(value = "客商委托书-添加", notes = "客商委托书-添加")
    @PostMapping(value = "/addProfilePoa")
    @RequiresPermissions("bcsPoa.add")
    public Result<?> addProfilePoa(@RequestBody ProfilePoa profilePoa) {
        CsFlag csFlag = CsFlag.bussiness;
        profilePoa.setCsFlag(csFlag.getCode());
        profilePoaService.save(profilePoa);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param profilePoa
     * @return
     */
    @AutoLog(value = "客商委托书-编辑")
    @ApiOperation(value = "客商委托书-编辑", notes = "客商委托书-编辑")
    @PutMapping(value = "/editProfilePoa")
    @RequiresPermissions("bcsPoa.edit")
    public Result<?> editProfilePoa(@RequestBody ProfilePoa profilePoa) {
        profilePoaService.updateById(profilePoa);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商委托书-通过id删除")
    @ApiOperation(value = "客商委托书-通过id删除", notes = "客商委托书-通过id删除")
    @DeleteMapping(value = "/deleteProfilePoa")
    @RequiresPermissions("bcsPoa.del")
    public Result<?> deleteProfilePoa(@RequestParam(name = "id", required = true) String id) {
        profilePoaService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客商委托书-批量删除")
    @ApiOperation(value = "客商委托书-批量删除", notes = "客商委托书-批量删除")
    @DeleteMapping(value = "/deleteBatchProfilePoa")
    @RequiresPermissions("bcsPoa.deleteBatch")
    public Result<?> deleteBatchProfilePoa(@RequestParam(name = "ids", required = true) String ids) {
        this.profilePoaService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }



    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfilePoa")
    @RequiresPermissions("bcsPoa.exportXls")
    public ModelAndView exportProfilePoa(HttpServletRequest request, ProfilePoa profilePoa) {
        // Step.1 组装查询条件
        QueryWrapper<ProfilePoa> queryWrapper = QueryGenerator.initQueryWrapper(profilePoa, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfilePoa> pageList = profilePoaService.list(queryWrapper);
        List<ProfilePoa> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客商委托书"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfilePoa.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客商委托书报表", "导出人:" + sysUser.getRealname(), "客商委托书"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfilePoa/{mainId}")
    @RequiresPermissions("bcsPoa.importExcel")
    public Result<?> importProfilePoa(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfilePoa> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfilePoa.class, params);
                for (ProfilePoa temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profilePoaService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-客商委托书-end----------------------------------------------*/


    /*--------------------------------子表处理-客商银行账户管理-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "客商银行账户管理-通过主表ID查询")
    @ApiOperation(value = "客商银行账户管理-通过主表ID查询", notes = "客商银行账户管理-通过主表ID查询")
    @GetMapping(value = "/listProfileBankByMainId")
    public Result<?> listProfileBankByMainId(ProfileBank profileBank,
                                             @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             HttpServletRequest req) {

        profileBank.setCsFlag(CsFlag.bussiness.getCode());
        QueryWrapper<ProfileBank> queryWrapper = QueryGenerator.initQueryWrapper(profileBank, req.getParameterMap());
        Page<ProfileBank> page = new Page<ProfileBank>(pageNo, pageSize);
        IPage<ProfileBank> pageList = profileBankService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param profileBank
     * @return
     */
    @AutoLog(value = "客商银行账户管理-添加")
    @ApiOperation(value = "客商银行账户管理-添加", notes = "客商银行账户管理-添加")
    @PostMapping(value = "/addProfileBank")
    @RequiresPermissions("bcsBank.add")
    public Result<?> addProfileBank(@RequestBody ProfileBank profileBank) {
        CsFlag csFlag = CsFlag.bussiness;
        profileBank.setCsFlag(csFlag.getCode());
        profileBankService.save(profileBank);

        profileBankService.insertResume(profileBank);

        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param profileBank
     * @return
     */
    @AutoLog(value = "客商银行账户管理-编辑")
    @ApiOperation(value = "客商银行账户管理-编辑", notes = "客商银行账户管理-编辑")
    @PutMapping(value = "/editProfileBank")
    @RequiresPermissions("bcsBank.edit")
    public Result<?> editProfileBank(@RequestBody ProfileBank profileBank) {
        profileBankService.updateById(profileBank);

        profileBankService.insertResume(profileBank);

        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商银行账户管理-通过id删除")
    @ApiOperation(value = "客商银行账户管理-通过id删除", notes = "客商银行账户管理-通过id删除")
    @DeleteMapping(value = "/deleteProfileBank")
    @RequiresPermissions("bcsBank.del")
    public Result<?> deleteProfileBank(@RequestParam(name = "id", required = true) String id) {

        profileBankService.removeById(id);

        //履历 主表以及客商等级表插入数据
        profileBankService.insertResume(profileBankService.getById(id));

        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客商银行账户管理-批量删除")
    @ApiOperation(value = "客商银行账户管理-批量删除", notes = "客商银行账户管理-批量删除")
    @DeleteMapping(value = "/deleteBatchProfileBank")
    @RequiresPermissions("bcsBank.deleteBatch")
    public Result<?> deleteBatchProfileBank(@RequestParam(name = "ids", required = true) String ids) {

        ProfileInf profileInf = profileBankService.getProfileRecordWhenDeleteBetch(ids);

        profileBankService.removeByIds(Arrays.asList(ids.split(",")));

        profileInfService.insertResume(profileInf);

        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfileBank")
    @RequiresPermissions("bcsBank.exportXls")
    public ModelAndView exportProfileBank(HttpServletRequest request, ProfileBank profileBank) {
        // Step.1 组装查询条件
        QueryWrapper<ProfileBank> queryWrapper = QueryGenerator.initQueryWrapper(profileBank, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfileBank> pageList = profileBankService.list(queryWrapper);
        List<ProfileBank> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客商银行账户管理"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfileBank.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客商银行账户管理报表", "导出人:" + sysUser.getRealname(), "客商银行账户管理"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfileBank/{mainId}")
    @RequiresPermissions("bcsBank.importExcel")
    public Result<?> importProfileBank(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfileBank> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfileBank.class, params);
                for (ProfileBank temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profileBankService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-客商银行账户管理-end----------------------------------------------*/

    /*--------------------------------子表处理-客商归属信息表-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "客商归属信息表-通过主表ID查询")
    @ApiOperation(value = "客商归属信息表-通过主表ID查询", notes = "客商归属信息表-通过主表ID查询")
    @GetMapping(value = "/listProfileBelongByMainId")
    public Result<?> listProfileBelongByMainId(ProfileBelong profileBelong,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) {
        profileBelong.setCsFlag("0");
        QueryWrapper<ProfileBelong> queryWrapper = QueryGenerator.initQueryWrapper(profileBelong, req.getParameterMap());
        Page<ProfileBelong> page = new Page<ProfileBelong>(pageNo, pageSize);
        IPage<ProfileBelong> pageList = profileBelongService.page(page, queryWrapper);

        //翻译 拼接 归属组织 并接收返回值
        List<ProfileBelong> profileBelongs = profileBelongService.translateGsCompany(pageList.getRecords());
        pageList.setRecords(profileBelongs);

        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param profileBelong
     * @return
     */
    @AutoLog(value = "客商归属信息表-添加")
    @ApiOperation(value = "客商归属信息表-添加", notes = "客商归属信息表-添加")
    @PostMapping(value = "/addProfileBelong")
    @RequiresPermissions("bcsBelong.add")
    public Result<?> addProfileBelong(@RequestBody ProfileBelong profileBelong) {
        CsFlag csFlag = CsFlag.bussiness;
        profileBelong.setCsFlag(csFlag.getCode());
        profileBelongService.save(profileBelong);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param profileBelong
     * @return
     */
    @AutoLog(value = "客商归属信息表-编辑")
    @ApiOperation(value = "客商归属信息表-编辑", notes = "客商归属信息表-编辑")
    @PutMapping(value = "/editProfileBelong")
    @RequiresPermissions("bcsBelong.edit")
    public Result<?> editProfileBelong(@RequestBody ProfileBelong profileBelong) {
        profileBelongService.updateById(profileBelong);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商归属信息表-通过id删除")
    @ApiOperation(value = "客商归属信息表-通过id删除", notes = "客商归属信息表-通过id删除")
    @DeleteMapping(value = "/deleteProfileBelong")
    @RequiresPermissions("bcsBelong.del")
    public Result<?> deleteProfileBelong(@RequestParam(name = "id", required = true) String id) {
        profileBelongService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客商归属信息表-批量删除")
    @ApiOperation(value = "客商归属信息表-批量删除", notes = "客商归属信息表-批量删除")
    @DeleteMapping(value = "/deleteBatchProfileBelong")
    @RequiresPermissions("bcsBelong.deleteBatch")
    public Result<?> deleteBatchProfileBelong(@RequestParam(name = "ids", required = true) String ids) {
        this.profileBelongService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfileBelong")
    @RequiresPermissions("bcsBelong.exportXls")
    public ModelAndView exportProfileBelong(HttpServletRequest request, ProfileBelong profileBelong) {
        // Step.1 组装查询条件
        QueryWrapper<ProfileBelong> queryWrapper = QueryGenerator.initQueryWrapper(profileBelong, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfileBelong> pageList = profileBelongService.list(queryWrapper);
        List<ProfileBelong> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客商归属信息表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfileBelong.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客商归属信息表报表", "导出人:" + sysUser.getRealname(), "客商归属信息表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfileBelong/{mainId}")
    @RequiresPermissions("bcsBelong.importExcel")
    public Result<?> importProfileBelong(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfileBelong> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfileBelong.class, params);
                for (ProfileBelong temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profileBelongService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-客商归属信息表-end----------------------------------------------*/

    /*--------------------------------子表处理-客商经营品种表-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "客商经营品种表-通过主表ID查询")
    @ApiOperation(value = "客商经营品种表-通过主表ID查询", notes = "客商经营品种表-通过主表ID查询")
    @GetMapping(value = "/listProfileProductByMainId")
    public Result<?> listProfileProductByMainId(ProfileProduct profileProduct,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        CsFlag csFlag = CsFlag.bussiness;
        profileProduct.setCsFlag(csFlag.getCode());
        QueryWrapper<ProfileProduct> queryWrapper = QueryGenerator.initQueryWrapper(profileProduct, req.getParameterMap());
        Page<ProfileProduct> page = new Page<ProfileProduct>(pageNo, pageSize);
        IPage<ProfileProduct> pageList = profileProductService.page(page, queryWrapper);

        //这个方法为翻译经营大类的方法
        List<ProfileProduct> profileProducts = profileProductService.getNameById(pageList.getRecords());

        pageList.setRecords(profileProducts);


        //以下为翻译省市
        List<ProfileProduct> profileProducts1 = profileProductService.translateProvinceOrCity(pageList.getRecords());

        pageList.setRecords(profileProducts1);

        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param profileProduct
     * @return
     */
    @AutoLog(value = "客商经营品种表-添加")
    @ApiOperation(value = "客商经营品种表-添加", notes = "客商经营品种表-添加")
    @PostMapping(value = "/addProfileProduct")
    @RequiresPermissions("bcsProduct.add")
    public Result<?> addProfileProduct(@RequestBody ProfileProduct profileProduct) {
        CsFlag csFlag = CsFlag.bussiness;
        profileProduct.setCsFlag(csFlag.getCode());

        //根据经营品种找到经营大类 并赋值
        profileProduct = profileProductService.findBusiVariety(profileProduct);

        profileProductService.save(profileProduct);
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param profileProduct
     * @return
     */
    @AutoLog(value = "客商经营品种表-编辑")
    @ApiOperation(value = "客商经营品种表-编辑", notes = "客商经营品种表-编辑")
    @PutMapping(value = "/editProfileProduct")
    @RequiresPermissions("bcsProduct.edit")
    public Result<?> editProfileProduct(@RequestBody ProfileProduct profileProduct) {

        //根据经营品种找到经营大类 并赋值
        profileProduct = profileProductService.findBusiVariety(profileProduct);

        profileProductService.updateById(profileProduct);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商经营品种表-通过id删除")
    @ApiOperation(value = "客商经营品种表-通过id删除", notes = "客商经营品种表-通过id删除")
    @DeleteMapping(value = "/deleteProfileProduct")
    @RequiresPermissions("bcsProduct.del")
    public Result<?> deleteProfileProduct(@RequestParam(name = "id", required = true) String id) {
        profileProductService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客商经营品种表-批量删除")
    @ApiOperation(value = "客商经营品种表-批量删除", notes = "客商经营品种表-批量删除")
    @DeleteMapping(value = "/deleteBatchProfileProduct")
    @RequiresPermissions("bcsProduct.deleteBatch")
    public Result<?> deleteBatchProfileProduct(@RequestParam(name = "ids", required = true) String ids) {
        this.profileProductService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfileProduct")
    @RequiresPermissions("bcsProduct.exportXls")
    public ModelAndView exportProfileProduct(HttpServletRequest request, ProfileProduct profileProduct) {
        // Step.1 组装查询条件
        QueryWrapper<ProfileProduct> queryWrapper = QueryGenerator.initQueryWrapper(profileProduct, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfileProduct> pageList = profileProductService.list(queryWrapper);
        List<ProfileProduct> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客商经营品种表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfileProduct.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客商经营品种表报表", "导出人:" + sysUser.getRealname(), "客商经营品种表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfileProduct/{mainId}")
    @RequiresPermissions("bcsProduct.importExcel")
    public Result<?> importProfileProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfileProduct> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfileProduct.class, params);
                for (ProfileProduct temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profileProductService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-客商经营品种表-end----------------------------------------------*/

    /*--------------------------------子表处理-供应商资质信息-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "供应商资质信息-通过主表ID查询")
    @ApiOperation(value = "供应商资质信息-通过主表ID查询", notes = "供应商资质信息-通过主表ID查询")
    @GetMapping(value = "/listProfileQualifiByMainId")
    public Result<?> listProfileQualifiByMainId(ProfileQualifi profileQualifi,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        profileQualifi.setCsFlag(CsFlag.bussiness.getCode());
        QueryWrapper<ProfileQualifi> queryWrapper = QueryGenerator.initQueryWrapper(profileQualifi, req.getParameterMap());
        Page<ProfileQualifi> page = new Page<ProfileQualifi>(pageNo, pageSize);
        IPage<ProfileQualifi> pageList = profileQualifiService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param profileQualifi
     * @return
     */
    @AutoLog(value = "供应商资质信息-添加")
    @ApiOperation(value = "供应商资质信息-添加", notes = "供应商资质信息-添加")
    @PostMapping(value = "/addProfileQualifi")
    @RequiresPermissions("bcsQualifi.add")
    public Result<?> addProfileQualifi(@RequestBody ProfileQualifi profileQualifi) {
        CsFlag csFlag = CsFlag.bussiness;
        profileQualifi.setCsFlag(csFlag.getCode());
        profileQualifiService.save(profileQualifi);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param profileQualifi
     * @return
     */
    @AutoLog(value = "供应商资质信息-编辑")
    @ApiOperation(value = "供应商资质信息-编辑", notes = "供应商资质信息-编辑")
    @PutMapping(value = "/editProfileQualifi")
    @RequiresPermissions("bcsQualifi.edit")
    public Result<?> editProfileQualifi(@RequestBody ProfileQualifi profileQualifi) {
        profileQualifiService.updateById(profileQualifi);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "供应商资质信息-通过id删除")
    @ApiOperation(value = "供应商资质信息-通过id删除", notes = "供应商资质信息-通过id删除")
    @DeleteMapping(value = "/deleteProfileQualifi")
    @RequiresPermissions("bcsQualifi.del")
    public Result<?> deleteProfileQualifi(@RequestParam(name = "id", required = true) String id) {
        profileQualifiService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "供应商资质信息-批量删除")
    @ApiOperation(value = "供应商资质信息-批量删除", notes = "供应商资质信息-批量删除")
    @DeleteMapping(value = "/deleteBatchProfileQualifi")
    @RequiresPermissions("bcsQualifi.deleteBatch")
    public Result<?> deleteBatchProfileQualifi(@RequestParam(name = "ids", required = true) String ids) {
        this.profileQualifiService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfileQualifi")
    @RequiresPermissions("bcsQualifi.exportXls")
    public ModelAndView exportProfileQualifi(HttpServletRequest request, ProfileQualifi profileQualifi) {
        // Step.1 组装查询条件
        QueryWrapper<ProfileQualifi> queryWrapper = QueryGenerator.initQueryWrapper(profileQualifi, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfileQualifi> pageList = profileQualifiService.list(queryWrapper);
        List<ProfileQualifi> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "供应商资质信息"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfileQualifi.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("供应商资质信息报表", "导出人:" + sysUser.getRealname(), "供应商资质信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfileQualifi/{mainId}")
    @RequiresPermissions("bcsQualifi.importExcel")
    public Result<?> importProfileQualifi(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfileQualifi> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfileQualifi.class, params);
                for (ProfileQualifi temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profileQualifiService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-供应商资质信息-end----------------------------------------------*/

    /*--------------------------------子表处理-客商类型等级表-begin----------------------------------------------*/

    /**
     * 通过主表ID查询
     *
     * @return
     */
    @AutoLog(value = "客商类型等级表-通过主表ID查询")
    @ApiOperation(value = "客商类型等级表-通过主表ID查询", notes = "客商类型等级表-通过主表ID查询")
    @GetMapping(value = "/listProfileTypeLevelByMainId")
    public Result<?> listProfileTypeLevelByMainId(ProfileTypeLevel profileTypeLevel,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        if(profileTypeLevel.getCsId()!=null){
            CsFlag csFlag = CsFlag.bussiness;
            profileTypeLevel.setCsFlag(csFlag.getCode());
            QueryWrapper<ProfileTypeLevel> queryWrapper = QueryGenerator.initQueryWrapper(profileTypeLevel, req.getParameterMap());
            Page<ProfileTypeLevel> page = new Page<ProfileTypeLevel>(pageNo, pageSize);
            IPage<ProfileTypeLevel> pageList = profileTypeLevelService.page(page, queryWrapper);
            return Result.OK(pageList);
        }else{
            Page<ProfileTypeLevel> page = new Page<ProfileTypeLevel>(pageNo, pageSize);
            IPage<ProfileTypeLevel> pageList = profileTypeLevelService.page(page, null);
            pageList.setRecords(null);
            return Result.OK(pageList);
        }

    }

    /**
     * 添加
     *
     * @param profileTypeLevel
     * @return
     */
    @AutoLog(value = "客商类型等级表-添加")
    @ApiOperation(value = "客商类型等级表-添加", notes = "客商类型等级表-添加")
    @PostMapping(value = "/addProfileTypeLevel")
    @RequiresPermissions("bcsTypeLevel.add")
    public Result<?> addProfileTypeLevel(@RequestBody ProfileTypeLevel profileTypeLevel) {
        CsFlag csFlag = CsFlag.bussiness;
        profileTypeLevel.setCsType(csFlag.getCode());
        profileTypeLevelService.save(profileTypeLevel);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param profileTypeLevel
     * @return
     */
    @AutoLog(value = "客商类型等级表-编辑")
    @ApiOperation(value = "客商类型等级表-编辑", notes = "客商类型等级表-编辑")
    @PutMapping(value = "/editProfileTypeLevel")
    @RequiresPermissions("bcsTypeLevel.edit")
    public Result<?> editProfileTypeLevel(@RequestBody ProfileTypeLevel profileTypeLevel) {

        profileTypeLevelService.updateById(profileTypeLevel);

        profileTypeLevelService.insertResume(profileTypeLevel);

        return Result.OK("编辑成功!");
    }


    /**
     * 查看履历
     *
     * @param profileTypeLevels
     * @return
     */
    @RequestMapping(value = "/resume")
    @RequiresPermissions("bcsTypeLevel.resume")
    public Result<?> recoverBlacklist(@RequestBody List<ProfileTypeLevel> profileTypeLevels) {

        List<ProfileTypeLevelHis> profileTypeLevelHisList =
                profileTypeLevelService.selectResume(profileTypeLevels.get(0).getCsId(), profileTypeLevels.get(0).getCsFlag());
        return Result.OK(profileTypeLevelHisList);

    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "客商类型等级表-通过id删除")
    @ApiOperation(value = "客商类型等级表-通过id删除", notes = "客商类型等级表-通过id删除")
    @DeleteMapping(value = "/deleteProfileTypeLevel")
    @RequiresPermissions("bcsTypeLevel.del")
    public Result<?> deleteProfileTypeLevel(@RequestParam(name = "id", required = true) String id) {
        profileTypeLevelService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "客商类型等级表-批量删除")
    @ApiOperation(value = "客商类型等级表-批量删除", notes = "客商类型等级表-批量删除")
    @DeleteMapping(value = "/deleteBatchProfileTypeLevel")
    @RequiresPermissions("bcsTypeLevel.deleteBatch")
    public Result<?> deleteBatchProfileTypeLevel(@RequestParam(name = "ids", required = true) String ids) {
        this.profileTypeLevelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     *
     * @return
     */
    @RequestMapping(value = "/exportProfileTypeLevel")
    @RequiresPermissions("bcsTypeLevel.exportXls")
    public ModelAndView exportProfileTypeLevel(HttpServletRequest request, ProfileTypeLevel profileTypeLevel) {
        // Step.1 组装查询条件
        QueryWrapper<ProfileTypeLevel> queryWrapper = QueryGenerator.initQueryWrapper(profileTypeLevel, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ProfileTypeLevel> pageList = profileTypeLevelService.list(queryWrapper);
        List<ProfileTypeLevel> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "客商类型等级表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ProfileTypeLevel.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客商类型等级表报表", "导出人:" + sysUser.getRealname(), "客商类型等级表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 导入
     *
     * @return
     */
    @RequestMapping(value = "/importProfileTypeLevel/{mainId}")
    @RequiresPermissions("bcsTypeLevel.importExcel")
    public Result<?> importProfileTypeLevel(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ProfileTypeLevel> list = ExcelImportUtil.importExcel(file.getInputStream(), ProfileTypeLevel.class, params);
                for (ProfileTypeLevel temp : list) {
                    temp.setCsId(mainId);
                }
                long start = System.currentTimeMillis();
                profileTypeLevelService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
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
        return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-客商类型等级表-end----------------------------------------------*/


}
